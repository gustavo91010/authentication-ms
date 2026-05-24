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

  private String app_name;

  private String password;

  private LocalDateTime lastLogin;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private boolean isActive;

  private UUID accessToken;

  private Set<Roles> roles = new HashSet<>();

  private Map<String, Object> otherFields;


}
