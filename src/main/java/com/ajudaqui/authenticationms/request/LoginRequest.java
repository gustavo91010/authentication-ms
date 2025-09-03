package com.ajudaqui.authenticationms.request;

import javax.validation.constraints.NotEmpty;

public class LoginRequest {
  @NotEmpty(message = "O campo email não pode ser vazio")
  private String email;
  @NotEmpty(message = "O campo password não pode ser vazio")
  private String password;

  private String application;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginRequest{email=" + email + ", password=" + password + ", application=" + application + "}";
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

}
