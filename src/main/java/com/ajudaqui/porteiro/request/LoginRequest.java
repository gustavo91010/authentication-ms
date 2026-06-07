package com.ajudaqui.porteiro.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
  @NotEmpty(message = "O campo email não pode ser vazio")
  private String email;
  @NotEmpty(message = "O campo password não pode ser vazio")
  private String password;

  @NotNull(message = "O campo appId deve esta presente")
  private String appId;

}
