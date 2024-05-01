/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.openfeign;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.urbanmarket.emailservice.dto.ResponseTransactionDto;

/**
 * Feign client for order service
 */
@FeignClient(name = "paymentservice", configuration = FeignClientConfig.class)
public interface PaymentClient {
	/**
	 * Get transaction by id
	 * 
	 * @param id transactionId
	 * @return responseTransactionDto
	 */
	@GetMapping("/api/payment/transaction/{id}")
	public ResponseTransactionDto getTransactionById(@PathVariable("id") UUID id);
}
