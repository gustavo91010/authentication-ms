package com.ajudaqui.porteiro.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.ajudaqui.porteiro.entity.Roles;
import com.ajudaqui.porteiro.entity.Users;
import com.ajudaqui.porteiro.entity.UsersAppData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersAppApplicationDto {

  private Long appId;
  private String appName;
  private String name;
  private String email;
  private boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime lastLogin;
  private Set<Roles> roles;
  private Map<String, Object> otherFields;

  public UsersAppApplicationDto(String appId, Users users) {
    UsersAppData appData = users.selectApp(appId);

    this.name = users.getName();
    this.email = users.getEmail();

    this.createdAt = users.getCreatedAt();
    this.isActive = appData.isActive();

    this.roles = appData.getRoles();
    if (appData.getLastLogin() != null)
      this.lastLogin = appData.getLastLogin();
    this.otherFields = appData.getOtherFields();
  }

  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public void setOtherFields(Map<String, Object> otherFields) {
    this.otherFields = otherFields;
  }
}
