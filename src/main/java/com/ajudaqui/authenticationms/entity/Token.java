package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "a_token")
public class Token {
  @Id
  private String id;

  private String token;

  private String userId;

  private LocalDateTime expirationDate;

  private LocalDateTime createdAt;

  public Token(String token, String userId, LocalDateTime expirationDate) {
    this.token = token;
    this.userId = userId;
    this.expirationDate = expirationDate;
    this.createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
  }



}
