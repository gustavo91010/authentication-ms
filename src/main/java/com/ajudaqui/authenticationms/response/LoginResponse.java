package com.ajudaqui.authenticationms.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.Roles;

public class LoginResponse {

  private Long id;
  private String name;
  private String email;
  private String jwt;
  private List<String> roles = new ArrayList<>();
  private Boolean active;
  private UUID access_token;
  private String application;

  public LoginResponse(UsersAppApplicationDto users, String jwt) {
    this.id = users.getUserId();
    this.name = users.getName();
    this.email = users.getEmail();
    this.active = users.isActive();
    this.roles = rolesToList(users.getRoles());
    this.jwt = jwt;
    this.access_token = users.getAccessTokne();
    this.application= users.getAppName();
  }

  private List<String> rolesToList(Set<Roles> roles) {
    return roles.stream()
        // .map(role -> "ROLE_" + role.getName())
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

}
