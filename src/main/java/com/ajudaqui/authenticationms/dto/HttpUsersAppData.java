package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.entity.UsersAppData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com os dados do usuário da aplicação")
public class HttpUsersAppData {

  @Schema(example = "10")
  private Long id;

  @Schema(example = "usuario@email.com")
  private String email;

  @Schema(example = "2026-02-27T21:15:00")
  private LocalDateTime lastLogin;

  @Schema(example = "2026-02-20T10:00:00")
  private LocalDateTime createdAt;

  @Schema(example = "2026-02-27T22:00:00")
  private LocalDateTime updatedAt;

  @Schema(example = "true")
  private boolean isActive;

  @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID accessToken;

  @Schema(example = "[\"ROLE_USER\", \"ROLE_MODERATOR\"]")
  private List<String> roles;

  public HttpUsersAppData(UsersAppData app) {
    this.id = app.getId();
    this.email = app.getUsers().getEmail();
    this.lastLogin = app.getLastLogin();
    this.createdAt = app.getCreatedAt();
    this.updatedAt = app.getUpdatedAt();
    this.isActive = app.isActive();
    this.accessToken = app.getAccessToken();
    this.roles = app.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList());
  }

  public HttpUsersAppData() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
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

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

}
