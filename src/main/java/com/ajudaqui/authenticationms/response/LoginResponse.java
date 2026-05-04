package com.ajudaqui.authenticationms.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.Roles;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta retornada após autenticação bem-sucedida")
public class LoginResponse {

  @Schema(example = "1", description = "ID do usuário")
  private Long id;

  @Schema(example = "João Silva", description = "Nome do usuário")
  private String name;

  @Schema(example = "usuario@email.com", description = "Email do usuário")
  private String email;

  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", description = "Token JWT para autenticação")
  private String jwt;

  @Schema(example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]", description = "Perfis de acesso do usuário")
  private List<String> roles = new ArrayList<>();

  @Schema(example = "true", description = "Indica se o usuário está ativo")
  private Boolean active;

  @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "Token de acesso da aplicação")
  private UUID access_token;

  @Schema(example = "aplicacao-name", description = "Nome da aplicação vinculada")
  private String application;

  @Schema(example = "{\"cpf\":\"123.345.568.90\", \"sexo\":\"m\"}", description = "Campos variavel dependendo da aplicação")
  private Map<String, Object> otherFields;

  public LoginResponse(UsersAppApplicationDto users, String jwt) {
    System.out.println("para o  LoginResponse "+users.getUserDataId());
    this.id = users.getUserDataId();
    this.name = users.getName();
    this.email = users.getEmail();
    this.active = users.isActive();
    this.roles = rolesToList(users.getRoles());
    this.jwt = jwt;
    this.access_token = users.getAccessTokne();
    this.application= users.getAppName();
    this.otherFields= users.getOtherFields();
  }

  private List<String> rolesToList(Set<Roles> roles) {
    return roles.stream()
        .map(role -> role.getName().name())
        .collect(Collectors.toList());
  }

  public LoginResponse() {
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

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public UUID getAccess_token() {
    return access_token;
  }

  public void setAccess_token(UUID access_token) {
    this.access_token = access_token;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public void setOtherFields(Map<String, Object> otherFields) {
    this.otherFields = otherFields;
  }

}
