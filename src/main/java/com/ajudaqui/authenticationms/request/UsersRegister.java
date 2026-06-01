package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.MessageException;

@Data
@NoArgsConstructor
public class UsersRegister {
  @NotBlank(message = "Campo nome é obrigatorio")
  private String name;
  @NotBlank(message = "Campo email é obrigatorio")
  private String email;
  @NotBlank(message = "Campo password é obrigatorio")
  private String password;
  @NotBlank(message = "Campo aooId é obrigatorio")
  private String appId;

  private Map<String, Object> payload;

  public Map<String, Object> getPayload() {
    return payload == null
        ? new HashMap<String, Object>()
        : payload;
  }

  public Users toUsers() {
    Users users = new Users();
    users.setName(this.name);
    users.setEmail(this.email);
    return users;
  }

  private String checkStrongPassword(String password) {
    if (password.length() < 7)
      throw new MessageException("A senha deve ter pelo menos 8 caracters");

    boolean isLowAndUpCase = password.matches("^(?=.*[a-z])(?=.*[A-Z]).+$");
    if (!isLowAndUpCase)
      throw new MessageException(
          "A senha deve ter pelo menos uma letra maiúscula, uma minuscula e um caracter especial ( @#$%&*_- )");

    boolean isCharacterEpecial = password.matches("^(?=.*[@#$%&*_-]).+$");

    if (!isCharacterEpecial)
      throw new MessageException("A senha deve ter pelo menos um caracter especial (@,#,$,%,&,*,-,_)");
    return new BCryptPasswordEncoder().encode(password);
  }

  // public Users toAppData(UsersRegister usersRegister, Set<Roles> roles) {
  public Users toAppData( Set<Roles> roles) {
    var user = this.toUsers();
    UsersAppData usersAppData = new UsersAppData();
    usersAppData.setRoles(roles);
    usersAppData.setAccessToken(UUID.randomUUID());
    usersAppData.setPassword(checkStrongPassword(this.password));
    usersAppData.setAppId(this.getAppId());
    usersAppData.setCreatedAt(LocalDateTime.now());
    usersAppData.setActive(true);
    user.getUsersAppData().add(usersAppData);

    return user;
  }
}
