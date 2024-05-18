/**
 * 18-May-2024
 * meeth
 */
package com.urbanmarket.userservice.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.urbanmarket.userservice.dto.RequestUserCredentialDto;

/**
 * Feign client for authentication service
 */
@FeignClient(name = "authenticationservice", configuration = FeignClientConfig.class)
public interface AuthenticationClient {

	/**
	 * Create user credentials
	 * 
	 * @param request credential data
	 */
	@PostMapping(value = "/api/authentication", consumes = "multipart/form-data")
	public void saveUser(RequestUserCredentialDto request);

	/**
	 * Delete user credentials
	 * 
	 * @param email user unique email
	 */
	@DeleteMapping("/api/authentication/{email}")
	public void deleteUser(@PathVariable("email") String email);
}
