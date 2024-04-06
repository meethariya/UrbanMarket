/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.urbanmarket.productservice.dto.RequestProductDto;
import com.urbanmarket.productservice.dto.ResponseProductDto;
import com.urbanmarket.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

/**
 * Controller layer for product.
 */
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	/**
	 * Create product.<br>
	 * Throws {@link org.springframework.http.converter.HttpMessageNotReadableException}, .
	 * {@link IOException}, {@link DuplicateKeyException}
	 * @param productDto requestProductDto
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createProduct(@ModelAttribute RequestProductDto productDto,@RequestParam(name = "images", required = false) MultipartFile[] files) throws IOException {
		productService.createProduct(productDto, files);
	}

	/**
	 * Get all products.
	 * @return list of responseProductDto
	 */
	@GetMapping
	public ResponseEntity<List<ResponseProductDto>> getAllProducts(){
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}

	/**
	 * Get product using id.<br>
	 * Throws {@link com.urbanmarket.productservice.exception.ProductNotFoundException}.
	 *
	 * @param id productId
	 * @return responseProductDto
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponseProductDto> getProductById(@PathVariable("id") String id){
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}

	/**
	 * Update product details.<br>
	 * Throws {@link com.urbanmarket.productservice.exception.ProductNotFoundException}  and
	 *  	  {@link org.springframework.http.converter.HttpMessageNotReadableException}.
	 * @param id productId
	 * @param productDto requestProductDto
	 * @return Updated responseProductDto
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable("id") String id, @ModelAttribute RequestProductDto productDto){
		return new ResponseEntity<>(productService.updateProduct(id, productDto), HttpStatus.ACCEPTED);
	}

	/**
	 * Delete product.
	 * @param id productId.
	 */
	@DeleteMapping("/{id}")
	public void deleteProductById(@PathVariable("id") String id){
		productService.deleteProduct(id);
	}
}
