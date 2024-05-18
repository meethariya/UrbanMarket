/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.userservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Request DTO for user credential
 */
@Data
@Builder
public class RequestUserCredentialDto {
	private String email;
	private String password;
	private String role;
}
