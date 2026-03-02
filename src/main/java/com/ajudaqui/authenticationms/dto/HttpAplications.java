package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import com.ajudaqui.authenticationms.entity.Applications;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da aplicação")
public class HttpAplications {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "aplication-name")
  private String name;

  @Schema(example = "https://app.com/register")
  private String registertUrl;

  @Schema(example = "https://app.com/redirect")
  private String redirectUrl;

  @Schema(example = "150")
  private int totalUsers;

  @Schema(example = "2026-02-27T22:30:00")
  private LocalDateTime createdAt;

  public HttpAplications(Applications applcations) {
    this.id = applcations.getId();
    this.name = applcations.getName();
    this.createdAt = applcations.getCreatedAt().withNano(0);
    if (applcations.getRegisterUrl() != null)
      this.registertUrl = applcations.getRegisterUrl();
    if (applcations.getRedirectUrl() != null)
      this.redirectUrl = applcations.getRedirectUrl();
    this.totalUsers = applcations.getUsersAppData().size();
  }

  public HttpAplications() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getRegistertUrl() {
    return registertUrl;
  }

  public void setRegistertUrl(String registertUrl) {
    this.registertUrl = registertUrl;
  }

  public int getTotalUsers() {
    return totalUsers;
  }

  public void setTotalUsers(int totalUsers) {
    this.totalUsers = totalUsers;
  }

}
