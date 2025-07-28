package com.ajudaqui.authenticationms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.response.error.ResponseError;
import com.ajudaqui.authenticationms.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
  Logger logger = LoggerFactory.getLogger(UsersController.class);

  @Autowired
  private UsersService usersService;

  @GetMapping("/authorization")
  public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String accessToken) {
    try {
      logger.info("[GET] | /users/authorization");
      return ResponseEntity.ok(usersService.findByAccessToken(accessToken));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable Long id) {
    try {

      logger.info("[GET] | /users/{id}", id);
      Users users = usersService.findById(id);
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @GetMapping("/jwt")
  public ResponseEntity<?> findByJwt(@RequestHeader("Authorization") String jwtToken) {
    try {
      logger.info("[GET] /users/jwt |");
      return ResponseEntity.ok(usersService.findByJwt(jwtToken));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @GetMapping()
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> findAll() {
    try {
      logger.info("[GET] | /users |");
      List<Users> users = usersService.findAll();
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @PutMapping("/active-a2f")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> getQrCode(
      @RequestHeader("Authorization") String jwtToken) {
    return ResponseEntity.ok(usersService.generatedQrCode(jwtToken));
  }
}
