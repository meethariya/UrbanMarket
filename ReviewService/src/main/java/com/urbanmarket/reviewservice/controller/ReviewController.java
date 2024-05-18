/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.controller;

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

import com.urbanmarket.reviewservice.dto.EditReviewDto;
import com.urbanmarket.reviewservice.dto.RequestReviewDto;
import com.urbanmarket.reviewservice.dto.ResponseReviewDto;
import com.urbanmarket.reviewservice.service.ReviewService;

import lombok.RequiredArgsConstructor;

/**
 * Controller layer for review
 */
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * Create review.
	 * 
	 * @param requestReviewDto requestDto
	 * @param multipartFiles   images
	 * @throws IOException
	 * @throws {@link      DuplicateKeyException}
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createReview(@ModelAttribute RequestReviewDto requestReviewDto,
			@RequestParam(name = "images", required = false) MultipartFile[] multipartFiles) throws IOException {
		reviewService.createReview(requestReviewDto, multipartFiles);
	}

	/**
	 * Get all reviews.
	 * 
	 * @return list of responseReviewDto
	 */
	@GetMapping
	public ResponseEntity<List<ResponseReviewDto>> getAllReviews() {
		return new ResponseEntity<>(reviewService.getAllReviews(), HttpStatus.OK);
	}

	/**
	 * Get all reviews of a product.
	 * 
	 * @param id productId
	 * @return list of responseReviewDto of product
	 */
	@GetMapping("/productId/{id}")
	public ResponseEntity<List<ResponseReviewDto>> getReviewByProduct(@PathVariable("id") String id) {
		return new ResponseEntity<>(reviewService.getReviewByProduct(id), HttpStatus.OK);
	}

	/**
	 * Get all reviews by a customer.
	 * 
	 * @param id customerId
	 * @return list of responseReviewDto by a customer
	 */
	@GetMapping("/customerId/{id}")
	public ResponseEntity<List<ResponseReviewDto>> getReviewByCustomer(@PathVariable("id") long id) {
		return new ResponseEntity<>(reviewService.getReviewByCustomer(id), HttpStatus.OK);
	}

	/**
	 * Get review by id
	 * 
	 * @param id reviewId
	 * @return responseReviewDto by id
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ResponseReviewDto> getReviewById(@PathVariable("id") String id) {
		return new ResponseEntity<>(reviewService.getReviewById(id), HttpStatus.OK);
	}

	/**
	 * Update review.
	 * 
	 * @param id        reviewId
	 * @param reviewDto editReviewDto
	 * @return updated responseReviewDto
	 * @throws IOException if files are not handled correctly
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ResponseReviewDto> updateReview(@PathVariable("id") String id,
			@ModelAttribute EditReviewDto reviewDto,
			@RequestParam(name = "images", required = false) MultipartFile[] files) throws IOException {
		return new ResponseEntity<>(reviewService.updateReview(id, reviewDto, files), HttpStatus.ACCEPTED);
	}

	/**
	 * Delete review by id
	 * 
	 * @param id reviewId
	 */
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteReview(@PathVariable("id") String id) {
		reviewService.deleteReview(id);
	}

	/**
	 * Delete reviews of a customer
	 * 
	 * @param id customerId
	 */
	@DeleteMapping("/customer/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteReviewOfCustomer(@PathVariable("id") long id) {
		reviewService.deleteByCustomerId(id);
	}

	/**
	 * Delete reviews of a product
	 * 
	 * @param id productId
	 */
	@DeleteMapping("/product/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteReviewOfProduct(@PathVariable("id") String id) {
		reviewService.deleteByProductId(id);
	}

	/**
	 * Get average rating of a product
	 * 
	 * @param productId id
	 * @return average rating
	 */
	@GetMapping("/averageRating/{id}")
	public ResponseEntity<Double> getAverageRating(@PathVariable("id") String productId) {
		return new ResponseEntity<>(reviewService.getAvgRating(productId), HttpStatus.OK);
	}
}
