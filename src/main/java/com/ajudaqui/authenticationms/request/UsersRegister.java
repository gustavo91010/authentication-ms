package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.MessageException;

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

  public Users toUsers(boolean isInternal) {
    Users users = new Users();
    users.setName(this.name);
    users.setEmail(this.email);
    // TODO esperara  implementacão do front
    // users.setActive(isInternal);
    users.setActive(true);
    return users;
  }

  public UsersAppData toAppData(Users users, boolean isInternal, Applications applications, Set<Roles> roles) {
    UsersAppData usersAppData = new UsersAppData();
    usersAppData.setUsers(users);
    usersAppData.setRoles(roles);
    usersAppData.setAccessToken(UUID.randomUUID());
    usersAppData.setPassword(checkStrongPassword(this.password));
    usersAppData.setApplications(applications);
    usersAppData.setCreatedAt(LocalDateTime.now());
    usersAppData.setActive(isInternal);
    return usersAppData;
  }

  private String checkStrongPassword(String password) {
    if (password.length() < 7)
      throw new MessageException("A senha deve ter pelo menos 8 caracters");

    boolean isLowAndUpCase = password.matches("^(?=.*[a-z])(?=.*[A-Z]).+$");
    if (!isLowAndUpCase)
      throw new MessageException("A senha deve ter pelo menos uma letra maiúscula e uma minuscula");

    boolean isCharacterEpecial = password.matches("^(?=.*[@#$%&*_-]).+$");

    if (!isCharacterEpecial)
      throw new MessageException("A senha deve ter pelo menos um caracter especial (@,#,$,%,&,*,-,_)");
    return new BCryptPasswordEncoder().encode(password);
  }
}
