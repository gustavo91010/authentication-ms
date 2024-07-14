package com.ajudaqui.authenticationms.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.response.MessageResponse;
import com.ajudaqui.authenticationms.service.AuthService;
import com.ajudaqui.authenticationms.service.model.ClientRegister;

@RestController
@RequestMapping("/auth")
public class AuthController {


	@Autowired
	private AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			LoginResponse userAuthenticated = authService.authenticateUser(loginRequest);

			return ResponseEntity.ok(userAuthenticated);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/signup-client")
	@PreAuthorize("hasRole('ROLE_ADMIN') ")
	public ResponseEntity<?> registerClient(
			@Valid @RequestBody ClientRegister usersRegister,
			@RequestHeader("Authorization") String jwtToken) {
		
		try {
			authService.registerClient(usersRegister);			
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/signup-customer")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_CLIENT')")
	public ResponseEntity<?> registerCustomer(
			@Valid @RequestBody CustomerRegister consumerRegister,
			@RequestHeader("Authorization") String jwtToken) {
		
		try {
			authService.registerCustomer(consumerRegister);			
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	


}
