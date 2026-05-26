package com.ajudaqui.authenticationms.response;

import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

public class UsersAppResponse {

  private String applications;

  private boolean isActive;

  private UUID accessToken;

  private Set<String> roles = new HashSet<>();

  private Map<String, Object> otherFields;

  public UsersAppResponse(Users user, String accessToken) {
    UsersAppData appData = user.selectApp(accessToken);
    this.applications = appData.getAppName();
    this.isActive = appData.isActive();
    this.roles = appData.getRoles().stream()
        .map(Roles::getName)
        .map(ERoles::name)
        .collect(Collectors.toSet());
    this.otherFields = appData.getOtherFields();
  }


  public String getApplications() {
    return applications;
  }

  public void setApplications(String applications) {
    this.applications = applications;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public UUID getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(UUID accessToken) {
    this.accessToken = accessToken;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public void setOtherFields(Map<String, Object> otherFields) {
    this.otherFields = otherFields;
  }

}
