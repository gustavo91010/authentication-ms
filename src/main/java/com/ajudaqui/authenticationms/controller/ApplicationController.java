package com.ajudaqui.authenticationms.controller;

import java.util.List;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.controller.doc.ApplicationsControllerDoc;
import com.ajudaqui.authenticationms.dto.*;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.service.ApplicationsService;

import com.ajudaqui.authenticationms.entity.UsersAppData;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
@RegisterReflectionForBinding({ ApplicationDto.class, HttpAplications.class, HttpUsersAppData.class })
public class ApplicationController implements ApplicationsControllerDoc {

  final private ApplicationsService applicationsService;
  final private JwtUtils jwtUtils;

  public ApplicationController(ApplicationsService applicationsService, JwtUtils jwtUtils) {
    this.applicationsService = applicationsService;
    this.jwtUtils = jwtUtils;
  }

  @Override
  public ResponseEntity<HttpAplications> regsiter(
      @RequestHeader("Authorization") String authorization,
      @RequestBody ApplicationDto appicationDto) {
    Applications regsiter = applicationsService.regsiter(authorization, appicationDto);
    return ResponseEntity.ok(new HttpAplications(regsiter));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public ResponseEntity<HttpAplications> getByAppName(@RequestHeader("Authorization") String jwtToken,
      @PathVariable String appName) {
    Applications aplicaiton = applicationsService.findByName(appName);
    return ResponseEntity.ok(new HttpAplications(aplicaiton));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public ResponseEntity<List<HttpAplications>> allApplications(
      @RequestHeader("Authorization") String authorization) {
    return ResponseEntity.ok(applicationsService.findAll(authorization));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public ResponseEntity<HttpAplications> getById(@RequestHeader("Authorization") String jwtToken,
      @PathVariable String applicationId) {
    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    Applications aplicaiton = applicationsService.findById(applicationId);
    return ResponseEntity.ok(new HttpAplications(aplicaiton));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public ResponseEntity<HttpAplications> update(
      @RequestHeader("Authorization") String jwtToken,
      @PathVariable String appName,
      @RequestBody ApplicationDto dto) {

    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    Applications aplicaiton = applicationsService.update(email, appName, dto);
    return ResponseEntity.ok(new HttpAplications(aplicaiton));
  }

  @PutMapping("/name/{appName}/assign-admin")
  @PreAuthorize("hasRole('ROLE_MODERATOR')")
  public ResponseEntity<HttpUsersAppData> assignAdmin(
      @RequestHeader("Authorization") String jwtToken,
      @PathVariable String appName,
      @RequestParam String email) {
    String moderatorEmail = jwtUtils.getEmailFromJwtToken(jwtToken);
    UsersAppData promoted = applicationsService.assignAdmin(moderatorEmail, appName, email);
    return ResponseEntity.ok(new HttpUsersAppData(promoted));
  }
}
