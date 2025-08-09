package com.ajudaqui.authenticationms.controller;

import java.util.List;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.dto.*;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.service.ApplicationsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
public class ApplicationController {

  final private ApplicationsService applicationsService;
  final private JwtUtils jwtUtils;

  public ApplicationController(ApplicationsService applicationsService, JwtUtils jwtUtils) {
    this.applicationsService = applicationsService;
    this.jwtUtils = jwtUtils;
  }

  @PostMapping("")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<HttpAplications> regsiter(@RequestBody ApplicationDto appicationDto) {
    Applications regsiter = applicationsService.regsiter(appicationDto);
    return ResponseEntity.ok(new HttpAplications(regsiter));
  }

  @GetMapping("/name/{appNAme}")
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public ResponseEntity<List<HttpUsersAppData>> getUsersByApp(@RequestHeader("Authorization") String jwtToken,
      @PathVariable String appNAme) {
    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    return ResponseEntity.ok(applicationsService.userByApp(email, appNAme));
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> allApplications() {
    return ResponseEntity.ok(applicationsService.findAll());
  }

  @GetMapping("/id/{applicationId}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Applications> getById(@RequestHeader("Authorization") String jwtToken,
      @PathVariable Long applicationId) {
    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    return ResponseEntity.ok(applicationsService.findById(email, applicationId));
  }
}
