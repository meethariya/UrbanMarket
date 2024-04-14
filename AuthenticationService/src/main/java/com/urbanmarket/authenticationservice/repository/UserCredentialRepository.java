/**
 * 14-Apr-2024
 * meeth
 */
package com.urbanmarket.authenticationservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urbanmarket.authenticationservice.model.UserCredential;

/**
 * Repository layer for user credential
 */
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

	/**
	 * Find user by email
	 * 
	 * @param email user's
	 * @return optional of userCredential
	 */
	public Optional<UserCredential> findByEmail(String email);

	/**
	 * Delete userCredentials by email.
	 * 
	 * @param email user's email
	 */
	public void deleteByEmail(String email);
}
