package com.ajudaqui.authenticationms.controller;

import javax.validation.Valid;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
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
  final private JwtUtils jwtUtils;

  public AuthController(AuthService authService, JwtUtils jwtUtils) {
    this.authService = authService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
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
      Boolean response = authService.confirmByToken(jwtUtils.getEmailFromJwtToken(jwtToken), token);
      return ResponseEntity.ok(new MessageResponse(response.toString()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage()));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(
      @Valid @RequestBody UsersRegister usersRegister) {
    try {
      logger.info(String.format("[POST] | auth/signup | email: " + usersRegister.getEmail()));
      return ResponseEntity.ok(authService.registerUser(usersRegister));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(
          String.format("Problema ao registrar o email %s, %s", usersRegister.getEmail(), e.getMessage())));
    }
  }

  @GetMapping("/permission/{token}")
  public ResponseEntity<?> verifyToken(@PathVariable String token) {
    try {
      logger.info("[GET] | /auth/permission/token/{}", token);
      boolean isAccess = authService.verifyToken(token);
      return ResponseEntity.ok(new AccessApi(isAccess));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(e.getMessage()));
    }
  }
}
