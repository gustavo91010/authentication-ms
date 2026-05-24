package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.UsersAppData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersAppApplicationDto {

  private Long userDataId;
  private String appName;
  private String name;
  private String email;
  private boolean isActive;
  private UUID accessTokne;
  private LocalDateTime createdAt;
  private LocalDateTime lastLogin;
  private Set<Roles> roles;
  private Map<String, Object> otherFields;

  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public void setOtherFields(Map<String, Object> otherFields) {
    this.otherFields = otherFields;
  }

  public UsersAppApplicationDto(UsersAppData usersAppData) {
    // this.userDataId = usersAppData.getId();
    // this.appName = usersAppData.getApplications().getName();
    // this.name = usersAppData.getUsers().getName();
    // this.email = usersAppData.getUsers().getEmail();
    this.createdAt = usersAppData.getCreatedAt();
    this.isActive = usersAppData.isActive();
    this.accessTokne = usersAppData.getAccessToken();

    this.roles = usersAppData.getRoles();
    if (usersAppData.getLastLogin() != null)
      this.lastLogin = usersAppData.getLastLogin();
    this.otherFields = usersAppData.getOtherFields();
  }

}
