/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.urbanmarket.reviewservice.dto.EditReviewDto;
import com.urbanmarket.reviewservice.dto.RequestReviewDto;
import com.urbanmarket.reviewservice.dto.ResponseReviewDto;
import com.urbanmarket.reviewservice.exception.ReviewNotFoundException;
import com.urbanmarket.reviewservice.model.Review;
import com.urbanmarket.reviewservice.repository.AverageRatingProjection;
import com.urbanmarket.reviewservice.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for review
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	private final MongoTemplate mongoTemplate;

	private final ModelMapper modelMapper;

	private final MessageSource source;

	@Value("${server.port}")
	String port;

	/**
	 * Create a review for a specific product by given user.<br>
	 * If it already exists, throws {@link DuplicateKeyException}.<br>
	 * Saves all the review images to static path and sets it url.
	 * 
	 * @param requestReviewDto request dto
	 * @param multipartFiles   images
	 * @throws IOException
	 */
	public void createReview(RequestReviewDto requestReviewDto, MultipartFile[] files) throws IOException {
		log.info("REVIEWSERVICE: Create review @PORT: " + port);
		Optional<Review> optionalReview = reviewRepository.findByProductIdAndCustomerId(requestReviewDto.getProductId(),
				requestReviewDto.getCustomerId());

		// check if the review already exists
		if (optionalReview.isPresent()) {
			throw new DuplicateKeyException("Review for product: " + requestReviewDto.getProductId() + " by customer: "
					+ requestReviewDto.getCustomerId() + " is already submitted");
		}

		Set<String> allImagePath = new HashSet<>();
		// saving all images to resources and setting its path in model
		if (files != null && files.length != 0) {
			for (int i = 0; i < files.length; i++) {
				String imagePath = saveImage(i + 1, requestReviewDto.getProductId(), requestReviewDto.getCustomerId(),
						files[i]);
				allImagePath.add(imagePath);
			}
		}
		requestReviewDto.setImagePath(allImagePath);
		// save model
		reviewRepository.save(requestToModel(requestReviewDto));
	}

	/**
	 * Get all reviews.
	 * 
	 * @return list of responseReviewDto.
	 */
	public List<ResponseReviewDto> getAllReviews() {
		log.info("REVIEWSERVICE: Get all reviews @PORT: " + port);
		return reviewRepository.findAll().stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get all reviews of a product.
	 * 
	 * @param productId id
	 * @return list of reviews of a product
	 */
	public List<ResponseReviewDto> getReviewByProduct(String productId) {
		log.info("REVIEWSERVICE: Get all reviews by productId @PORT: " + port);
		return reviewRepository.findByProductId(productId).stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get all reviews by a customer.
	 * 
	 * @param customerId id
	 * @return list of reviews by a customer.
	 */
	public List<ResponseReviewDto> getReviewByCustomer(long customerId) {
		log.info("REVIEWSERVICE: Get all reviews by customerId @PORT: " + port);
		return reviewRepository.findByCustomerId(customerId).stream().map(this::modelToResponse).toList();
	}

	/**
	 * Get review by id.
	 * 
	 * @throws ReviewNotFoundException
	 * @param reviewId id
	 * @return response review dto by id
	 */
	public ResponseReviewDto getReviewById(String reviewId) {
		log.info("REVIEWSERVICE: Get all reviews by id @PORT: " + port);
		return modelToResponse(reviewRepository.findById(reviewId)
				.orElseThrow(() -> new ReviewNotFoundException("No Review found with id: " + reviewId)));
	}

	/**
	 * Delete review by id.<br>
	 * <b>Silently ignore if not found</b>
	 * 
	 * @param reviewId id
	 */
	public void deleteReview(String reviewId) {
		// check for product and delete it's images
		reviewRepository.findById(reviewId).ifPresent(review -> {
			String path = review.getImagePath().iterator().next();
			try {
				FileUtils.deleteDirectory(new File(path).getParentFile());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		});
		reviewRepository.deleteById(reviewId);
	}

	/**
	 * Update review using id. If images are added, replaces the old with new ones.
	 * 
	 * @param id        reviewId
	 * @param reviewDto editReviewDto
	 * @param files     images
	 * @return updated responseReviewDto
	 * @throws IOException if files are not saved correctly
	 */
	public ResponseReviewDto updateReview(String id, EditReviewDto reviewDto, MultipartFile[] files)
			throws IOException {
		// check if review exists.
		Review review = reviewRepository.findById(id)
				.orElseThrow(() -> new ReviewNotFoundException("No Review found with id: " + id));
		// update new rating and comments.
		review.setComment(reviewDto.getComment());
		review.setRating(reviewDto.getRating());
		review.setModifiedOn(new Date());
		// update image if any exists.
		if (files != null && files.length > 0) {
			Set<String> allImagePath = new HashSet<>();
			for (int i = 0; i < files.length; i++) {
				String imagePath = saveImage(i + 1, review.getProductId(), review.getCustomerId(), files[i]);
				allImagePath.add(imagePath);
			}
			review.setImagePath(allImagePath);
		}
		return modelToResponse(reviewRepository.save(review));
	}

	/**
	 * Find average rating of a product. Using aggregation, find by id, group them
	 * and find avg on rating as 'averageRating'. Returns avg as 0 when productId is
	 * not found.
	 * 
	 * @param productId id
	 * @return average rating
	 */
	public Double getAvgRating(String productId) {
		// Create an aggregation pipeline
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("productId").is(productId)),
				Aggregation.group("productId").avg("rating").as("averageRating"));

		// Perform aggregation
		AggregationResults<AverageRatingProjection> results = mongoTemplate.aggregate(aggregation, "review",
				AverageRatingProjection.class);
		AverageRatingProjection ratingProjection = results.getUniqueMappedResult();
		return ratingProjection != null ? ratingProjection.getAverageRating() : 0;
	}

	/**
	 * Delete all reviews of a customer
	 * 
	 * @param id customerId
	 */
	public void deleteByCustomerId(long id) {
		List<ResponseReviewDto> reviewByCustomer = getReviewByCustomer(id);
		for (ResponseReviewDto r : reviewByCustomer)
			deleteReview(r.getId());
	}

	/**
	 * Delete all reviews of a product
	 * 
	 * @param id productId
	 */
	public void deleteByProductId(String id) {
		List<ResponseReviewDto> reviewByProduct = getReviewByProduct(id);
		for (ResponseReviewDto r : reviewByProduct)
			deleteReview(r.getId());
	}

	/**
	 * Converts request review dto to review model. Sets modified date as current
	 * date.
	 * 
	 * @param reviewDto requestReviewDto
	 * @return Review model
	 */
	private Review requestToModel(RequestReviewDto reviewDto) {
		Review review = modelMapper.map(reviewDto, Review.class);
		review.setModifiedOn(new Date());
		review.setId(null);
		return review;
	}

	/**
	 * Converts review model to responseReviewDto
	 * 
	 * @param review model
	 * @return responsereviewDto
	 */
	private ResponseReviewDto modelToResponse(Review review) {
		return modelMapper.map(review, ResponseReviewDto.class);
	}

	/**
	 * @return path of review picture folder
	 */
	private String getReviewPicFolderPath() {
		return source.getMessage("reviewFolder", null, Locale.ENGLISH);
	}

	/**
	 * Saves an image to resources and returns it entire path.<br>
	 * Name consists of Product id which creates a sub-folder and customer's id for
	 * all its images.<br>
	 * Index is used as name of file, to store in its folder sequentially.<br>
	 * 
	 * @param imageIndex fileName
	 * @param productId  folderPath
	 * @param customerId folderPath
	 * @param file       image
	 * @return imagePath
	 * @throws IOException
	 */
	private String saveImage(int imageIndex, String productId, long customerId, MultipartFile file) throws IOException {
		// folder's path = static default path + product's name and brand + customer's
		// id
		String folderPath = getReviewPicFolderPath().concat(productId);
		folderPath = folderPath + File.separator + customerId;
		// set file name as index + the extension on original file
		String imageName = file.getOriginalFilename();
		String fileName;
		// if in case original file name isn't fetched, use default .jpg
		if (imageName == null) {
			fileName = imageIndex + ".jpg";
		} else {
			fileName = imageIndex + "." + imageName.split("[.]")[1];
		}
		// set file path as folderPath + filename
		String filePath = folderPath + File.separator + fileName;
		filePath = filePath.replace('/', File.separator.charAt(0));
		// create file directory if it doesn't exist
		File dir = new File(folderPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// save file
		Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		return filePath;
	}

}
