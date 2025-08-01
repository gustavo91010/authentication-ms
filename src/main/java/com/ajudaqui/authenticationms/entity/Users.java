package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Campo nome não pode estar vazio")
  @Size(max = 100)
  private String name;
  @NotBlank(message = "Campo email não pode estar vazio")
  @Size(max = 50)
  private String email;
  @NotBlank(message = "Campo password não pode estar vazio")
  @Size(max = 120)
  private String password;
  @NotNull
  private Boolean active;
  private LocalDateTime created_at;
  private LocalDateTime update_at;
  private String access_token;
  private String aplication;
  private String secret;
  private Boolean a2fActive;

  public Users() {
  }

  public Users(String name, String email, String password, String aplication) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.aplication = aplication;
    this.active = false;
    this.created_at = LocalDateTime.now();
    this.update_at = LocalDateTime.now();
    this.secret = UUID.randomUUID().toString();
    this.a2fActive = false;
    this.access_token = UUID.randomUUID().toString();
  }

  public String getAplication() {
    return aplication;
  }

  public void setAplication(String aplication) {
    this.aplication = aplication;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Roles> roles = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Set<Roles> getRoles() {
    return roles;
  }

  public void setRoles(Set<Roles> roles) {
    this.roles = roles;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public LocalDateTime getCreated_at() {
    return created_at;
  }

  public void setCreated_at(LocalDateTime created_at) {
    this.created_at = created_at;
  }

  public LocalDateTime getUpdate_at() {
    return update_at;
  }

  public void setUpdate_at(LocalDateTime update_at) {
    this.update_at = update_at;
  }

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  @Override
  public String toString() {
    return "Users [id=" + id + ", email=" + email + ", password=" + password + ", active=" + active
        + ", created_at=" + created_at + ", update_at=" + update_at + ", access_token=" + access_token
        + ", aplication=" + aplication + ", roles=" + roles + "]";
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

  public Boolean getA2fActive() {
    return a2fActive;
  }

  public void setA2fActive(Boolean a2fActive) {
    this.a2fActive = a2fActive;
  }

}
