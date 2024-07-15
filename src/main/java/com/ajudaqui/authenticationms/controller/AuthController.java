package com.ajudaqui.authenticationms.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.response.MessageResponse;
import com.ajudaqui.authenticationms.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	Logger logger = LoggerFactory.getLogger(AuthController.class);


	@Autowired
	private AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		String msg= "Solocitação de login ";
		try {
			LoginResponse userAuthenticated = authService.authenticateUser(loginRequest);

			logger.info(msg+ "recebida com sucesso");
			return ResponseEntity.ok(userAuthenticated);

		} catch (Exception e) {
			logger.error(msg +"recusada por motivo de: "+e.getMessage());

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PostMapping("/signup")
	@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_MODERATOR')")
	public ResponseEntity<?> registerUser(
			@Valid @RequestBody UsersRegister usersRegister) {
		
		try {
			authService.registerUser(usersRegister);			
			return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	


}
