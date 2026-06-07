package com.ajudaqui.porteiro.controller;

import jakarta.validation.Valid;

import com.ajudaqui.porteiro.controller.doc.AuthControllerDoc;
import com.ajudaqui.porteiro.request.LoginRequest;
import com.ajudaqui.porteiro.request.UsersRegister;
import com.ajudaqui.porteiro.response.*;
import com.ajudaqui.porteiro.response.error.ResponseError;
import com.ajudaqui.porteiro.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RegisterReflectionForBinding({UsersRegister.class, LoginRequest.class, LoginResponse.class, MessageResponse.class, ResponseError.class})
public class AuthController implements AuthControllerDoc {
  Logger logger = LoggerFactory.getLogger(AuthController.class);

  final private AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Override
  public ResponseEntity<LoginResponse> registerUser(
      UsersRegister usersRegister) {
    logger.info(String.format("[POST] | auth/signup | email: " + usersRegister.getEmail()));
    return ResponseEntity.ok(authService.registerUser(usersRegister));
  }

  @PostMapping("/signin/by-token/{token}")
  public ResponseEntity<LoginResponse> loginByToken(@RequestParam String token) {
    LoginResponse userAuthenticated = authService.loginByToken(token);
    logger.info("[POST] | auth/signin | email: " + userAuthenticated.getEmail());
    return ResponseEntity.ok(userAuthenticated);
  }

  @Override
  public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest) {
    LoginResponse userAuthenticated = authService.authenticateUser(loginRequest);
    logger.info("[POST] | auth/signin | email: " + loginRequest.getEmail());
    return ResponseEntity.ok(userAuthenticated);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> confirmToken(
      String jwtToken,
      String token) {
    logger.info("[POST] | auth/confirm-token | token: " + token);
    try {
      Boolean response = authService.confirmByToken(jwtToken, token);
      return ResponseEntity.ok(new MessageResponse(response.toString()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage()));
    }
  }

}
