package com.ajudaqui.authenticationms.controller;

import com.ajudaqui.authenticationms.response.error.ResponseError;
import com.ajudaqui.authenticationms.service.UsersAppDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
  Logger logger = LoggerFactory.getLogger(UsersController.class);

  @Autowired
  private UsersAppDataService userApp = new UsersAppDataService();

  @GetMapping("/email/{email}")
  public ResponseEntity<?> findById(@PathVariable String email,
      @RequestParam(required = false, defaultValue = "bill-manager") String application) {
    try {
      logger.info("[GET] | /users/email/{email}", email);
      return ResponseEntity.ok(userApp.getUsersByEmail(email, application));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable Long userAppId) {
    try {
      logger.info("[GET] | /users/{userAppId}", userAppId);
      return ResponseEntity.ok(userApp.findByUsersId(userAppId));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @GetMapping("/authorization")
  public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String accessToken) {
    try {
      logger.info("[GET] | /users/authorization");
      return ResponseEntity.ok(userApp.getData(accessToken));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }
}
