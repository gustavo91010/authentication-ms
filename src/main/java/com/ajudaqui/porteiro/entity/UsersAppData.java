package com.ajudaqui.porteiro.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersAppData {

  private String appId;
  private String appName;

  private String password;

  private LocalDateTime lastLogin;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private boolean isActive;

  private UUID accessToken;
  // @Builder.Default // TODO sera que iss da??
  private Set<Roles> roles = new HashSet<>();

  private Map<String, Object> otherFields;

  public UsersAppData newApp(String appId, String appName, String password, boolean active, Set<Roles> roles) {
    UsersAppData NewApp = new UsersAppData();
    NewApp.setAppId(appId);
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
