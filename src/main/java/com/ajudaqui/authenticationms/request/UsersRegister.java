package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;

public class UsersRegister {
  @NotBlank(message = "Campo nome é obrigatorio")
  private String name;
  @NotBlank(message = "Campo email é obrigatorio")
  private String email;
  @NotBlank(message = "Campo password é obrigatorio")
  private String password;
  @NotBlank(message = "Campo aplication é obrigatorio")
  private String aplication;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getAplication() {
    return aplication;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setAplication(String aplication) {
    this.aplication = aplication;
  }

  public Users toUsers() {
    Users users = new Users();
    users.setName(this.name);
    users.setEmail(this.email);
    return users;
  }

  public UsersAppData toAppData(Long userId,Long appId, Set<Roles> roles) {
    UsersAppData usersAppData = new UsersAppData();
    usersAppData.setUserId(userId);
    usersAppData.setRoles(roles);
    usersAppData.setPassword(new BCryptPasswordEncoder().encode(this.password));
    usersAppData.setAppId(appId);
    usersAppData.setCreatedAt(LocalDateTime.now());
    usersAppData.setActive(false);
    return usersAppData;
  }
}
