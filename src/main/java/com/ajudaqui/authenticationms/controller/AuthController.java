package com.ajudaqui.authenticationms.controller;

import javax.validation.Valid;

import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.AccessApi;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.response.MessageResponse;
import com.ajudaqui.authenticationms.response.error.ResponseError;
import com.ajudaqui.authenticationms.service.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse userAuthenticated = authService.authenticateUser(loginRequest);

    logger.info("[POST] | auth/signin | email: " + loginRequest.getEmail());
    return ResponseEntity.ok(userAuthenticated);

  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(
      @Valid @RequestBody UsersRegister usersRegister) {
    try {
      logger.info(String.format("[POST] | auth/signup | email: " + usersRegister.getEmail()));

      authService.registerUser(usersRegister);
      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(
          String.format("Problema ao registrar o email %s, %s", usersRegister.getEmail(), e.getMessage())));
    }
  }

  @GetMapping("/permission/{token}")
  public ResponseEntity<?> verifyToken(@PathVariable String token) {
    try {

      logger.info("[GET] | /auth/token/{}", token);
      boolean isAccess = authService.verifyToken(token);
      return ResponseEntity.ok(new AccessApi(isAccess));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseError(e.getMessage()));
    }
  }
}
