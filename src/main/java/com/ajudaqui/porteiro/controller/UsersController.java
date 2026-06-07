package com.ajudaqui.porteiro.controller;

import java.util.UUID;

import com.ajudaqui.porteiro.entity.Users;
import com.ajudaqui.porteiro.response.UsersAppResponse;
import com.ajudaqui.porteiro.response.error.ResponseError;
import com.ajudaqui.porteiro.service.UsersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RegisterReflectionForBinding({ UsersAppResponse.class, ResponseError.class })
public class UsersController {
  Logger logger = LoggerFactory.getLogger(UsersController.class);

  @Autowired
  private UsersService userApp;

  @GetMapping("/{accessToken}")
  public ResponseEntity<?> findById(@PathVariable String accessToken) {
    try {
      logger.info("[GET] | /users/{accessToken}", accessToken);
      Users user = userApp.findByAccessToken(UUID.fromString(accessToken));
      return ResponseEntity.ok(new UsersAppResponse(user, accessToken));
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
