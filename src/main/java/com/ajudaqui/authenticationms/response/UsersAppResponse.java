package com.ajudaqui.authenticationms.response;

import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import lombok.Data;

@Data
public class UsersAppResponse {

  private String applications;

  private boolean isActive;

  private UUID accessToken;

  private Set<String> roles = new HashSet<>();

  private Map<String, Object> otherFields;

  public UsersAppResponse(Users user, String accessToken) {
    UsersAppData appData = user.selectApp(accessToken);
    this.applications = appData.getAppId();
    this.isActive = appData.isActive();
    this.roles = appData.getRoles().stream()
        .map(Roles::getName)
        .map(ERoles::name)
        .collect(Collectors.toSet());
    this.otherFields = appData.getOtherFields();
  }


}
