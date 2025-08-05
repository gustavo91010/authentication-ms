package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;

public class UsersAppApplicationDto {

  private Long userId;
  private Long appId;
  private String name;
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime lastLogin;

  public UsersAppApplicationDto(UsersAppData usersAppData, String name, String email) {
    this.userId = usersAppData.getUserId();
    this.appId = usersAppData.getAppId();
    this.name = name;
    this.email = email;
    this.createdAt = usersAppData.getCreatedAt();
    if (usersAppData.getLastLogin() != null)
      this.lastLogin = usersAppData.getLastLogin();
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getAppId() {
    return appId;
  }

  public void setAppId(Long appId) {
    this.appId = appId;
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

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

}
