package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ajudaqui.authenticationms.entity.Applications;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
public class ApplicationDto {

  @Email
  @NotBlank(message = "Email do moderador é obrigatório")
  private String emailModerador;
  @NotBlank(message = "O ID da aplicação do moderador é obrigatória")
  private String appIdOfModerador;
  @NotBlank(message = "Nome é obrigatório")
  private String name;
  @Schema(description = "A campo loginByTokenUrl é responsavel pela confirmação do email no registro")
  private String loginByTokenUrl;
  @Schema(description = "A campo registerUrl(IP da aplicação) é utilizado para o envio dos dados no registro")
  private String registerUrl;
  @Schema(description = "Campo para fazer a decodificação do jwt")
  private String secret;

  public Applications toEntity() {
    Applications applcations = new Applications(this.name, this.secret);

    if (this.registerUrl != null && !this.registerUrl.isEmpty())
      applcations.setRegisterUrl(this.registerUrl);

    if (this.loginByTokenUrl != null && !this.loginByTokenUrl.isEmpty())
      applcations.setLoginByTokenUrl(this.loginByTokenUrl);
    return applcations;
  }

  public Applications toUpdate(Applications applications) {

    if (this.name != null && !this.name.isEmpty())
      applications.setName(this.name);
    if (this.secret != null && !this.secret.isEmpty())
      applications.setSecretId(this.secret);
    if (this.loginByTokenUrl != null && !this.loginByTokenUrl.isEmpty())
      applications.setLoginByTokenUrl(this.loginByTokenUrl);
    if (this.registerUrl != null && !this.registerUrl.isEmpty())
      applications.setRegisterUrl(this.registerUrl);

    applications.setUpdatedAt(LocalDateTime.now());
    return applications;
  }


}
