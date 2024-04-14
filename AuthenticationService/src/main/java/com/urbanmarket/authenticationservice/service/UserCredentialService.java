/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.service;

import java.security.Key;
import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.urbanmarket.authenticationservice.dto.RequestUserCredentialDto;
import com.urbanmarket.authenticationservice.dto.ResponseUserCredentialDto;
import com.urbanmarket.authenticationservice.exception.CredentialNotFoundException;
import com.urbanmarket.authenticationservice.model.Role;
import com.urbanmarket.authenticationservice.model.UserCredential;
import com.urbanmarket.authenticationservice.repository.UserCredentialRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for user credential
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserCredentialService implements UserDetailsService {

	private final UserCredentialRepository credentialRepository;

	private final ModelMapper modelMapper;

	private final PasswordEncoder passwordEncoder;

	@Value("${server.port}")
	private int port;

	/**
	 * JWT secret token
	 */
	@Value("${JWT.secret}")
	private String secret;

	/**
	 * Get UserCredentials from database by email
	 * 
	 * @param username user email
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("AUTHENTICATIONSERVICE: Load user credential @PORT: " + port);

		return credentialRepository.findByEmail(username)
				.orElseThrow(() -> new CredentialNotFoundException("No user found with email:" + username));
	}

	/**
	 * Create user credentials.
	 * 
	 * @param credentialDto requestUserCredentialDto
	 */
	public void createCredential(RequestUserCredentialDto credentialDto) {
		log.info("AUTHENTICATIONSERVICE: Create user credential @PORT: " + port);

		credentialRepository.save(requestToModel(credentialDto));
	}

	/**
	 * Update user password.
	 * 
	 * @param credentialDto details
	 * @return updated responseUserCredentialDto
	 * @throws CredentialNotFoundException
	 */
	public ResponseUserCredentialDto updateCredential(RequestUserCredentialDto credentialDto) {
		log.info("AUTHENTICATIONSERVICE: Update user credential @PORT: " + port);
		// checks if user credential exists or not
		UserCredential credential = credentialRepository.findByEmail(credentialDto.getEmail()).orElseThrow(
				() -> new CredentialNotFoundException("No user found with email:" + credentialDto.getEmail()));
		// updates password
		UserCredential updatedCredential = requestToModel(credentialDto);
		credential.setPassword(updatedCredential.getPassword());

		return modelToResponse(credentialRepository.save(credential));
	}

	/**
	 * Delete user credentials
	 * 
	 * @param email user's
	 */
	public void deleteCredential(String email) {
		log.info("AUTHENTICATIONSERVICE: Delete user credential @PORT: " + port);

		credentialRepository.deleteByEmail(email);
	}

	/**
	 * Validate Token.
	 * 
	 * @param token JWT token
	 */
	public void validateToken(String token) {
		log.info("AUTHENTICATIONSERVICE: Validate token @PORT: " + port);

		Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
	}

	/**
	 * Generate Token. If user is logged in by google, set default role as
	 * customer<br>
	 * Else check its authorities and set role accordingly
	 * 
	 * @param principal UserCred by OAuth2
	 * @param auth      UserCred by spring authentication
	 * @return Token
	 */
	public String generateToken(OAuth2User principal, Authentication auth) {
		log.info("AUTHENTICATIONSERVICE: Generate token @PORT: " + port);

		String role = Role.CUSTOMER.name();
		String email;
		if (principal != null) {
			email = (String) principal.getAttribute("email");
		} else {
			email = auth.getName();
			role = auth.getAuthorities().iterator().next().toString();
		}

		return createToken(email, role);
	}

	/**
	 * Generate token that is set to expire after 30 mins.
	 * 
	 * @param claims   headers
	 * @param username userCred name
	 * @return token
	 */
	private String createToken(String username, String role) {
		return Jwts.builder().claim("role", role).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Converts request userCredential dto to model.<br>
	 * Hashes password as well.
	 * 
	 * @param credentialDto requestDto
	 * @return userCredential model
	 */
	private UserCredential requestToModel(RequestUserCredentialDto credentialDto) {
		UserCredential userCredential = modelMapper.map(credentialDto, UserCredential.class);
		userCredential.setPassword(passwordEncoder.encode(credentialDto.getPassword()));
		return userCredential;
	}

	/**
	 * Converts userCredential mode to response dto
	 * 
	 * @param userCredential model
	 * @return ResponseUserCredentialDto
	 */
	private ResponseUserCredentialDto modelToResponse(UserCredential userCredential) {
		return modelMapper.map(userCredential, ResponseUserCredentialDto.class);
	}

	/**
	 * Get key using SECRET.
	 * 
	 * @return signature key
	 */
	private Key getSignKey() {
		byte[] signKey = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(signKey);
	}
}
