/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Value("${server.port}")
	String port;

	public void createProduct(RequestProductDto productDto) {
		log.info("PRODUCTSERVICE: Create product @PORT: " + port);
		productRepository.save(requestToModel(productDto));
	}

	public List<ResponseProductDto> getAllProducts() {
		log.info("PRODUCTSERVICE: Get all product @PORT: " + port);
		return productRepository.findAll().stream().map(this::modelToResponse).toList();
	}

	public ResponseProductDto getProductById(String id) {
		log.info("PRODUCTSERVICE: Get product by id @PORT: " + port);
		return modelToResponse(productRepository.findById(id).orElseThrow(ProductNotFoundException::new));
	}
	
	public ResponseProductDto updateProduct(String id, RequestProductDto productDto) {
		log.info("PRODUCTSERVICE: Update product @PORT: " + port);
		Product newProduct = requestToModel(productDto);
		Product savedProduct = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
		newProduct.setId(savedProduct.getId());
		return modelToResponse(productRepository.save(newProduct));
	}

	public void deleteProduct(String id) {
		log.info("PRODUCTSERVICE: Delete product @PORT: " + port);
		productRepository.deleteById(id);
	}
	
	private Product requestToModel(RequestProductDto request) {
		return modelMapper.map(request, Product.class);
	}

	private ResponseProductDto modelToResponse(Product product) {
		return modelMapper.map(product, ResponseProductDto.class);
	}

}
