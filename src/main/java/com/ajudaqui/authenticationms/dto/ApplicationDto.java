package com.ajudaqui.authenticationms.dto;

import javax.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.Applcations;

public class ApplicationDto {

  @NotBlank(message = "Campo nome é obrigatorio")
  private String name;
  private String redirectUrl;
  private String secret;

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

  public Applcations toEntity() {
    Applcations applcations = new Applcations(this.name, this.secret);
    if (!this.redirectUrl.isEmpty())
      applcations.setRedirectUrl(this.redirectUrl);
    return applcations;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

}
