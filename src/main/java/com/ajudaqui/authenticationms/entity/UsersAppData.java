package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersAppData {

  private String appName;

  private String password;

  private LocalDateTime lastLogin;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private boolean isActive;

  private UUID accessToken;

  private Set<Roles> roles = new HashSet<>();

  private Map<String, Object> otherFields;

  public UsersAppData newApp(String appName, String password, boolean active, Set<Roles> roles) {
    UsersAppData NewApp = new UsersAppData();
    NewApp.setAppName(appName);
    NewApp.setPassword(password);
    NewApp.setActive(active);
    NewApp.setRoles(roles);
    NewApp.setAccessToken(UUID.randomUUID());
    NewApp.setCreatedAt(LocalDateTime.now());
    NewApp.setUpdatedAt(LocalDateTime.now());
    return NewApp;
  }


}
