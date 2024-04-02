/**
 * 31-Mar-2024
 * meeth
 */
package com.urbanmarket.productservice.model;

import java.util.Set;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product Model class
 */
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'name':1 ,'brand':1}",unique = true)
public class Product {
	@MongoId
	private String id;
	private String name;
	private String brand;
	private double price;
	private Set<String> imageUrl;
	private Category category;
}