package com.ajudaqui.authenticationms.controller;

import java.util.List;

import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.response.UsersAppResponse;
import com.ajudaqui.authenticationms.response.error.ResponseError;
import com.ajudaqui.authenticationms.service.UsersAppDataService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RegisterReflectionForBinding({UsersAppResponse.class, ResponseError.class})
public class UsersController {
  Logger logger = LoggerFactory.getLogger(UsersController.class);

  @Autowired
  private UsersAppDataService userApp;

  @GetMapping("/email/{email}")
  public ResponseEntity<?> findById(@PathVariable String email,
      @RequestParam(required = false, defaultValue = "bill-manager") String application) {
    try {
      logger.info("[GET] | /users/email/{email}", email);
      List<UsersAppData> user = userApp.getAllUsersByEmail(email);
      return ResponseEntity.ok(user.stream()
          .map(UsersAppResponse::new));
    } catch (Exception e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseError(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable Long userAppId) {
    try {
      logger.info("[GET] | /users/{userAppId}", userAppId);
      UsersAppData user = userApp.findByUsersId(userAppId);
      return ResponseEntity.ok(new UsersAppResponse(user));
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
