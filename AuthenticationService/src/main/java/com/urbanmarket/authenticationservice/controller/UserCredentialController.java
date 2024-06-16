/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urbanmarket.authenticationservice.dto.JwtTokenDto;
import com.urbanmarket.authenticationservice.dto.RequestUserCredentialDto;
import com.urbanmarket.authenticationservice.dto.ResponseUserCredentialDto;
import com.urbanmarket.authenticationservice.service.UserCredentialService;

import lombok.RequiredArgsConstructor;

/**
 * Controller for Authentication Service.
 */
@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class UserCredentialController {

	private final UserCredentialService credentialService;

	/**
	 * Save UserCredentials.
	 * 
	 * @param request DTO
	 * @return ResponseDto for UserCredential
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void saveUser(@ModelAttribute RequestUserCredentialDto request) {
		credentialService.createCredential(request);
	}

	/**
	 * Delete credentials
	 * 
	 * @param email user's
	 */
	@DeleteMapping("/{email}")
	@ResponseStatus(code = HttpStatus.ACCEPTED)
	public void deleteUser(@PathVariable("email") String email) {
		credentialService.deleteCredential(email);
	}

	/**
	 * Update credential password
	 * 
	 * @param credentialDto requestDto
	 * @return updated credentials
	 */
	@PutMapping
	public ResponseEntity<ResponseUserCredentialDto> updateCredentials(
			@ModelAttribute RequestUserCredentialDto credentialDto) {
		return new ResponseEntity<>(credentialService.updateCredential(credentialDto), HttpStatus.OK);
	}

	/**
	 * Generate token.
	 * 
	 * @param principal authentication details
	 * @return token
	 * @throws UserCredentialNotFoundException
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, path = "/generateToken")
	public ResponseEntity<JwtTokenDto> generateToken(@AuthenticationPrincipal OAuth2User principal,
			Authentication auth) {
		// validate if the user trying to generate token has valid credentials.
		return new ResponseEntity<>(credentialService.generateToken(principal, auth), HttpStatus.CREATED);
	}

	/**
	 * Validate token.
	 * 
	 * @param token
	 */
	@GetMapping("/validateToken/{token}")
	@ResponseStatus(code = HttpStatus.OK)
	public void validateToken(@PathVariable("token") String token) {
		credentialService.validateToken(token);
	}

}
