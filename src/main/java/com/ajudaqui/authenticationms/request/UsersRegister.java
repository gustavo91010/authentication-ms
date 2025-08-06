package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.Applcations;
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
    users.setActive(false);
    return users;
  }

  public UsersAppData toAppData(Users users, Applcations applications, Set<Roles> roles) {
    UsersAppData usersAppData = new UsersAppData();
    usersAppData.setUsers(users);
    usersAppData.setRoles(roles);
    usersAppData.setAccessToken(UUID.randomUUID()); // TODO se quebrar, sei que é aqui
    usersAppData.setPassword(new BCryptPasswordEncoder().encode(this.password));
    usersAppData.setApplications(applications);
    usersAppData.setCreatedAt(LocalDateTime.now());
    usersAppData.setActive(false);
    return usersAppData;
  }
}
