/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.urbanmarket.emailservice.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

/**
 * Controller layer for email
 */
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;

	/**
	 * On success order send receipt as email
	 * 
	 * @param id
	 * @throws IOException
	 * @throws MessagingException
	 * @throws MailException
	 */
	@GetMapping("/order-receipt/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void orderReceipt(@PathVariable UUID id) throws MailException, MessagingException, IOException {
		emailService.sendOrderReceipt(id);
	}

	/**
	 * Verify email using OTP before completing registration process
	 * 
	 * @param email user email
	 * @param name  user name
	 * @return OTP
	 * @throws MailException
	 * @throws MessagingException
	 */
	@GetMapping("/verify-email")
	public ResponseEntity<String> getMethodName(@RequestParam("email") String email, @RequestParam("name") String name)
			throws MailException, MessagingException {
		return new ResponseEntity<>(emailService.verifyEmail(email, name), HttpStatus.OK);
	}

	/**
	 * Welcome to application email.
	 * 
	 * @param id user id
	 * @throws MessagingException
	 * @throws MailException
	 */
	@GetMapping("/welcome/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void welcome(@PathVariable("id") Long id) throws MailException, MessagingException {
		emailService.welcome(id);
	}
}
