package com.ajudaqui.authenticationms.response;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

public class UsersAppResponse {

  private Long id;
  private Long userId;

  private String applications;

  private boolean isActive;

  private UUID accessToken;

  private Set<String> roles = new HashSet<>();

  private Map<String, Object> otherFields;

  public UsersAppResponse(UsersAppData user) {
    this.id = user.getId();
    this.userId = user.getUsers().getId();
    this.applications = user.getApplications().getName();
    this.isActive = user.isActive();
    this.roles = user.getRoles().stream()
        .map(Roles::getName)
        .map(ERoles::name)
        .collect(Collectors.toSet());
    this.otherFields = user.getOtherFields();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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
