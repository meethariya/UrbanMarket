/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createProduct(@RequestBody RequestProductDto productDto) {
		productService.createProduct(productDto);
	}

	@GetMapping
	public ResponseEntity<List<ResponseProductDto>> getAllProducts(){
		return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseProductDto> getProductById(@PathVariable("id") String id){
		return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable("id") String id, @RequestBody RequestProductDto productDto){
		return new ResponseEntity<>(productService.updateProduct(id, productDto), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/{id}")
	public void deleteProductById(@PathVariable("id") String id){
		productService.deleteProduct(id);
	}
}
