package com.ajudaqui.authenticationms.dto;

import com.ajudaqui.authenticationms.entity.Applications;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Resposta da aplicação")
public class HttpAplications {

  @Schema(example = "1")
  private String id;

  @Schema(example = "aplication-name")
  private String name;

  @Schema(example = "https://app.com/register")
  private String registertUrl;

  @Schema(example = "https://app.com/redirect")
  private String loginByTokenUrl;

  @Schema(example = "150")
  private int totalUsers;

  public HttpAplications(Applications applcations) {
    this.id = applcations.getId();
    this.name = applcations.getName();
    if (applcations.getRegisterUrl() != null)
      this.registertUrl = applcations.getRegisterUrl();
    if (applcations.getLoginByTokenUrl() != null)
      this.loginByTokenUrl = applcations.getLoginByTokenUrl();
  }

}
