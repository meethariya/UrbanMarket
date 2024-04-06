/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.urbanmarket.productservice.dto.RequestProductDto;
import com.urbanmarket.productservice.dto.ResponseProductDto;
import com.urbanmarket.productservice.exception.ProductNotFoundException;
import com.urbanmarket.productservice.model.Product;
import com.urbanmarket.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for product
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	private final ModelMapper modelMapper;

	private final MessageSource source;

	@Value("${server.port}")
	String port;

	/**
	 * Create product.<br>
	 * Convert requestProductDto to product model then save it.
	 * @param productDto requestProductDto
	 * @throws IOException 
	 */
	public void createProduct(RequestProductDto productDto, MultipartFile[] files) throws IOException{
		log.info("PRODUCTSERVICE: Create product @PORT: " + port);

		Optional<Product> nameAndBrand = productRepository.findByNameAndBrand(productDto.getName(), productDto.getBrand());
		if(nameAndBrand.isPresent()) {
			throw new DuplicateKeyException("Product with name: "+productDto.getName()+" and brand: "+productDto.getBrand()+" already exists");
		}
		
		Set<String> allImagePath = new HashSet<>();
		// saving all images to resources and setting its path in model
		if (files == null || files.length == 0) {
			String path = getProductPicFolderPath() + "default" + File.separator + "product.png";
			allImagePath.add(path);
		} else {
			for (int i = 0; i < files.length; i++) {
				String imagePath = saveImage(i + 1, productDto.getName()+productDto.getBrand(), files[i]);
				allImagePath.add(imagePath);
			}
		}
		productDto.setImageUrl(allImagePath);
		
		Product product = productRepository.save(requestToModel(productDto));
		productRepository.save(product);
	}

	/**
	 * Get all products and return in form of responseProductDto.
	 * @return list of responseProductDto
	 */
	public List<ResponseProductDto> getAllProducts() {
		log.info("PRODUCTSERVICE: Get all product @PORT: " + port);
		return productRepository.findAll().stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get product by id, and return in form of responseProductDto.
	 * @param id productId
	 * @return responseProductDto.
	 */
	public ResponseProductDto getProductById(String id) {
		log.info("PRODUCTSERVICE: Get product by id @PORT: " + port);
		return modelToResponse(productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("No product found with id: "+id)));
	}

	/**
	 * Update product details.<br>
	 * Get product details by id, and replace its values by new details.<br>
	 * Renames it image folder directory.
	 * Return updated value in form of responseProductDto
	 * @param id productId
	 * @param productDto requestProductDto
	 * @return responseProductDto
	 */
	public ResponseProductDto updateProduct(String id, RequestProductDto productDto) {
		log.info("PRODUCTSERVICE: Update product @PORT: " + port);
		// new input product
		Product newProduct = requestToModel(productDto);
		// existing product
		Product savedProduct = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("No product found with id: "+id));
		// product with same name and brand.
		Optional<Product> byNameAndBrand = productRepository.findByNameAndBrand(productDto.getName(), productDto.getBrand());
		// throw exception if another product with same name and brand exists
		if(byNameAndBrand.isPresent() && !byNameAndBrand.get().getId().equals(savedProduct.getId())) {
			throw new DuplicateKeyException("Product with name: "+productDto.getName()+" and brand: "+productDto.getBrand()+" already exists");
		}
		// renaming image's folder name
		File folder = new File(savedProduct.getImageUrl().iterator().next()).getParentFile();
		File newFolder = new File(folder.getParent()+File.separator+newProduct.getName()+newProduct.getBrand());
		boolean renameTo = folder.renameTo(newFolder);
		if(renameTo) {
			Set<String> newImagePath = Arrays.stream(newFolder.listFiles()).map(File::getAbsolutePath).collect(Collectors.toSet());
			// set new imagePaths and id
			newProduct.setImageUrl(newImagePath);
		} else {
			newProduct.setImageUrl(savedProduct.getImageUrl());
		}
		newProduct.setId(savedProduct.getId());
		// return updated dto
		return modelToResponse(productRepository.save(newProduct));
	}

	/**
	 * Delete product by id.<br>
	 * Also deletes its images.<br>
	 * <b>Silently ignores if no product found</b>
	 * @param id productId
	 */
	public void deleteProduct(String id) {
		log.info("PRODUCTSERVICE: Delete product @PORT: " + port);
		// check for product and delete it's images
		productRepository.findById(id).ifPresent(product->{
			String path = product.getImageUrl().iterator().next();
			try {
				FileUtils.deleteDirectory(new File(path).getParentFile());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
		productRepository.deleteById(id);
	}

	/**
	 * Converts requestProductDto to product model.
	 * @param request requestProductDto
	 * @return product model
	 */
	private Product requestToModel(RequestProductDto request) {
		return modelMapper.map(request, Product.class);
	}

	/**
	 * Converts product model to responseProductDto.
	 * @param product model
	 * @return responseProductDto
	 */
	private ResponseProductDto modelToResponse(Product product) {
		return modelMapper.map(product, ResponseProductDto.class);
	}

	/**
	 * @return path of product picture folder
	 */
	private String getProductPicFolderPath() {
		return source.getMessage("productFolder", null, Locale.ENGLISH);
	}
	
	/**
	 * Saves an image to resources and returns it entire path.<br>
	 * Name consists of Product name and brand which creates a sub-folder
	 * for all its images.<br>
	 * Index is used as name of file, to store in its folder sequentially.<br>
	 * @param imageIndex fileName
	 * @param productNameBrand folderPath
	 * @param file image
	 * @return imagePath
	 * @throws IOException
	 */
	private String saveImage(int imageIndex, String productNameBrand, MultipartFile file) throws IOException {
		// folder's path = static default path + product's name and brand
		String folderPath = getProductPicFolderPath().concat(productNameBrand);
		// set file name as index + the extension on original file
		String imageName = file.getOriginalFilename();
		String fileName;
		// if in case original file name isn't fetched, use default .jpg
		if(imageName==null){
			fileName = imageIndex + ".jpg";
		} else {
			fileName = imageIndex + "." + imageName.split("[.]")[1];
		}
		// set file path as folderPath + filename
		String filePath = folderPath + File.separator + fileName;
		filePath = filePath.replace('/',File.separator.charAt(0));
		// create file directory if it doesn't exist
		File dir = new File(folderPath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		// save file
		Files.copy(file.getInputStream(), Paths.get(filePath));
		return filePath;
	}

}
