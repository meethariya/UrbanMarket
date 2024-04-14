/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.dto;

import com.urbanmarket.authenticationservice.model.Role;

import lombok.Data;

/**
 * Response DTO for user credential
 */
@Data
public class ResponseUserCredentialDto {
	private long id;
	private String email;
	private Role role;
}
