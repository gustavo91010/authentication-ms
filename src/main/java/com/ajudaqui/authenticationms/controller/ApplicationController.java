package com.ajudaqui.authenticationms.controller;

import java.util.List;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.controller.doc.ApplicationsControllerDoc;
import com.ajudaqui.authenticationms.dto.ApplicationDto;
import com.ajudaqui.authenticationms.dto.HttpAplications;
import com.ajudaqui.authenticationms.dto.HttpUsersAppData;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.service.ApplicationsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
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
  public ResponseEntity<List<HttpUsersAppData>> getByAppName(@RequestHeader("Authorization") String jwtToken,
      @PathVariable String appName) {
    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    return ResponseEntity.ok(applicationsService.userByApp(email, appName));
  }

  @Override
  // @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<List<HttpAplications>> allApplications(
      @RequestHeader("Authorization") String authorization) {
    return ResponseEntity.ok(applicationsService.findAll(authorization));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<HttpAplications> getById(@RequestHeader("Authorization") String jwtToken,
      @PathVariable Long applicationId) {
    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    Applications aplicaiton = applicationsService.findById(email, applicationId);
    return ResponseEntity.ok(new HttpAplications(aplicaiton));
  }

  @Override
  // @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<HttpAplications> update(
      @RequestHeader("Authorization") String jwtToken,
      @PathVariable Long applicationId,
      @RequestBody ApplicationDto dto) {

    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    Applications aplicaiton = applicationsService.update(email, applicationId, dto);
    return ResponseEntity.ok(new HttpAplications(aplicaiton));
  }
}
