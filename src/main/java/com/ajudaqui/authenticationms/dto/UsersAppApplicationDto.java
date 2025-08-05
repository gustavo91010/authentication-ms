package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.UsersAppData;

public class UsersAppApplicationDto {

  private Long userId;
  private String appName;
  private String name;
  private String email;
  private boolean isActive;
  private UUID accessTokne;
  private LocalDateTime createdAt;
  private LocalDateTime lastLogin;
  private Set<Roles> roles;

  public UsersAppApplicationDto(UsersAppData usersAppData) {
    this.userId = usersAppData.getUsers().getId();
    this.appName = usersAppData.getApplications().getName();
    this.name = usersAppData.getUsers().getName();
    this.email = usersAppData.getUsers().getEmail();
    this.createdAt = usersAppData.getCreatedAt();
    this.isActive = usersAppData.isActive();
    this.accessTokne = usersAppData.getAccessToken();
    this. roles = usersAppData.getRoles();
    if (usersAppData.getLastLogin() != null)
      this.lastLogin = usersAppData.getLastLogin();
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public UUID getAccessTokne() {
    return accessTokne;
  }

  public void setAccessTokne(UUID accessTokne) {
    this.accessTokne = accessTokne;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Set<Roles> getRoles() {
    return roles;
  }

  public void setRoles(Set<Roles> roles) {
    this.roles = roles;
  }

}
