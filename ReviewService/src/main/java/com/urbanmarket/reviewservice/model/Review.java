/**
 * 07-Apr-2024
 * meeth
 */
package com.urbanmarket.reviewservice.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Review model
 */
@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(def = "{'productId':1 ,'customerId':1}",unique = true)
public class Review {
	@MongoId
	private String id;
	private String productId;
	private long customerId;
	private Set<String> imagePath;
	private byte rating;
	private String comment;
	private Date modifiedOn;
}
