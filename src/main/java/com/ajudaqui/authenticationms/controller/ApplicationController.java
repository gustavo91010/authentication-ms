package com.ajudaqui.authenticationms.controller;

import com.ajudaqui.authenticationms.dto.ApplicationDto;
import com.ajudaqui.authenticationms.dto.HttpAplications;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.service.ApplicationsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app")
public class ApplicationController {

  @Autowired
  private ApplicationsService applicationsService;

  @PostMapping("")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<HttpAplications> regsiter(@RequestBody ApplicationDto appicationDto) {
    Applications regsiter = applicationsService.regsiter(appicationDto);
    return ResponseEntity.ok(new HttpAplications(regsiter));
  }

}
