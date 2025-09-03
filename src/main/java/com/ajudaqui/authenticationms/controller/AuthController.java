package com.ajudaqui.authenticationms.controller;

import javax.validation.Valid;

import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.*;
import com.ajudaqui.authenticationms.response.error.ResponseError;
import com.ajudaqui.authenticationms.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
  Logger logger = LoggerFactory.getLogger(AuthController.class);

  final private AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/signin")
  public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse userAuthenticated = authService.authenticateUser(loginRequest);
    logger.info("[POST] | auth/signin | email: " + loginRequest.getEmail());
    return ResponseEntity.ok(userAuthenticated);
  }

  @PutMapping("/confirm-token")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> confirmToken(
      @RequestHeader("Authorization") String jwtToken,
      @RequestParam String token) {
    logger.info("[POST] | auth/confirm-token | token: " + token);
    try {
      Boolean response = authService.confirmByToken(jwtToken, token);
      return ResponseEntity.ok(new MessageResponse(response.toString()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage()));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(
      @Valid @RequestBody UsersRegister usersRegister) {
    // try {
      logger.info(String.format("[POST] | auth/signup | email: " + usersRegister.getEmail()));
      return ResponseEntity.ok(authService.registerUser(usersRegister));
    // } catch (Exception e) {
    //   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(
    //       String.format("Problema ao registrar o email %s, %s", usersRegister.getEmail(), e.getMessage())));
    // }
  }

}
