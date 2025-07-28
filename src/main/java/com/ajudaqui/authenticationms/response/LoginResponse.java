package com.ajudaqui.authenticationms.response;

import java.util.ArrayList;
import java.util.List;

import com.ajudaqui.authenticationms.entity.Users;

public class LoginResponse {

  private String name;
  private String email;
  private String jwt;
  private List<String> roles = new ArrayList<>();
  private Boolean active;
  private String access_token;

  public LoginResponse(Users user, List<String> roles, String jwt2) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.active = user.getActive();
    this.roles = roles;
    this.jwt = jwt2;
    this.access_token = user.getAccess_token();
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

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

}
