/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.dto;

import com.urbanmarket.authenticationservice.model.Role;

import lombok.Data;

/**
 * Request DTO for user credential
 */
@Data
public class RequestUserCredentialDto {
	private String email;
	private String password;
	private Role role;
}
