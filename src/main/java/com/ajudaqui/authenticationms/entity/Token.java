package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "a_token")
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @Column(name = "user_id", unique = true)
  private Long userId;

  @Column(name = "expiration_date")
  private LocalDateTime expirationDate;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public Token(String token, Long userId, LocalDateTime expirationDate) {
    this.token = token;
    this.userId = userId;
    this.expirationDate = expirationDate;
  }

  public Token() {
  }

  public Long getId() {

    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

}
