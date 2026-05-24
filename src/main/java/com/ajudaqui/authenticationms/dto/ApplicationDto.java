package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.Applications;

import io.swagger.v3.oas.annotations.media.Schema;

public class ApplicationDto {

  @Email
  @NotBlank(message = "Email é obrigatório")
  private String emailModerador;
  @NotBlank(message = "Aplicação do moderador é obrigatória")
  private String applicationOfModerador;
  @NotBlank(message = "Nome é obrigatório")
  private String name;
  @Schema(description = "A campo redirectUrl é utilizado para o redirecionamento no caso de auth2")
  private String redirectUrl;
  @Schema(description = "A campo registerUrl(IP da aplicação) é utilizado para o envio dos dados no registro")
  private String registerUrl;
  @Schema(description = "Campo para fazer a decodificação do jwt")
  private String secret;

  public ApplicationDto() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public Applications toEntity() {
    Applications applcations = new Applications(this.name, this.secret);

    if (this.registerUrl != null && !this.registerUrl.isEmpty())
      applcations.setRegisterUrl(this.registerUrl);

    if (this.redirectUrl != null && !this.redirectUrl.isEmpty())
      applcations.setRedirectUrl(this.redirectUrl);
    return applcations;
  }

  public Applications toUpdate(Applications applications) {

    if (this.name != null && !this.name.isEmpty())
      applications.setName(this.name);
    if (this.secret != null && !this.secret.isEmpty())
      applications.setSecretId(this.secret);
    if (this.redirectUrl != null && !this.redirectUrl.isEmpty())
      applications.setRedirectUrl(this.redirectUrl);
    if (this.registerUrl != null && !this.registerUrl.isEmpty())
      applications.setRegisterUrl(this.registerUrl);

    applications.setUpdatedAt(LocalDateTime.now());
    return applications;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getRegisterUrl() {
    return registerUrl;
  }

  public void setRegisterUrl(String registerUrl) {
    this.registerUrl = registerUrl;
  }

  public String getEmailModerador() {
    return emailModerador;
  }

  public void setEmailModerador(String emailModerador) {
    this.emailModerador = emailModerador;
  }

  public String getApplicationOfModerador() {
    return applicationOfModerador;
  }

  public void setApplicationOfModerador(String applicationOfModerador) {
    this.applicationOfModerador = applicationOfModerador;
  }

}
