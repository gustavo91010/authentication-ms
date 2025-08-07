package com.ajudaqui.authenticationms.dto;

import java.time.LocalDateTime;

import com.ajudaqui.authenticationms.entity.Applications;

public class HttpAplications {
  private Long id;
  private String name;
  private String redirectUrl;
  private LocalDateTime createdAt;

  public HttpAplications(Applications applcations) {
    this.id = applcations.getId();
    this.name = applcations.getName();
    this.createdAt = applcations.getCreatedAt();
    if (applcations.getRedirectUrl() != null)
      this.redirectUrl = applcations.getRedirectUrl();
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

}
