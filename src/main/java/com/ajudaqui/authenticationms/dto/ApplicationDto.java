package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import com.ajudaqui.authenticationms.entity.Applications;

public class ApplicationDto {

  private String name;
  private String redirectUrl;
  private String registerUrl;
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

  public Applications toEntity() {
    Applications applcations = new Applications(this.name, this.secret);

    if (this.registerUrl != null && !this.registerUrl.isEmpty())
      applcations.setRedirectUrl(redirectUrl);

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

}
