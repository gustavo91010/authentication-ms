package com.ajudaqui.authenticationms.controller;

import com.ajudaqui.authenticationms.service.ApplicationsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ApplicationController {

  @Autowired
  private ApplicationsService applicationsService;

  @GetMapping("/email/{email}")
  public ResponseEntity<?> findById(@PathVariable String email) {
    return ResponseEntity.ok("");
  }

}
