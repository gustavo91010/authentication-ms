package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import com.ajudaqui.authenticationms.entity.Applications;

public class HttpAplications {
  private Long id;
  private String name;
  private String registertUrl;
  private String redirectUrl;
  private int totalUsers;
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
