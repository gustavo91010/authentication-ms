package com.ajudaqui.porteiro.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ajudaqui.porteiro.entity.UsersAppData;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta com os dados do usuário da aplicação")
public class HttpUsersAppData {

  @Schema(example = "10")
  private String id;

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
    this.lastLogin = app.getLastLogin();
    this.createdAt = app.getCreatedAt();
    this.updatedAt = app.getUpdatedAt();
    this.isActive = app.isActive();
    this.accessToken = app.getAccessToken();
    this.roles = app.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList());
  }


}
