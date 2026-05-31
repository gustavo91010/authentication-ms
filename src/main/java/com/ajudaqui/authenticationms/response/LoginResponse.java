package com.ajudaqui.authenticationms.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.Roles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Resposta retornada após autenticação bem-sucedida")
public class LoginResponse {

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

  @Schema(example = "{\"cpf\":\"123.345.568.90\", \"sexo\":\"m\"}", description = "Campos variavel dependendo da aplicação")
  private Map<String, Object> otherFields;

  public LoginResponse(UsersAppApplicationDto users, String jwt) {
    this.name = users.getName();
    this.email = users.getEmail();
    this.active = users.isActive();
    this.roles = rolesToList(users.getRoles());
    this.jwt = jwt;
    this.otherFields = users.getOtherFields();
  }

  private List<String> rolesToList(Set<Roles> roles) {
    return roles.stream()
        .map(role -> role.getName().name())
        .collect(Collectors.toList());
  }

}
