/**
 * 15-Jun-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * ResponseDto class to return JWT
 */
@Data
@Builder
public class JwtTokenDto {

	private String token;
}
