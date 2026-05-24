package com.ajudaqui.authenticationms.entity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "applications")
public class Applications {

  @Id
  private String id;

  @Indexed(unique = true)
  @Size(max = 50)
  @NotBlank(message = "Campo email não pode estar vazio")
  private String name;

  private String secretId;

  private String registerUrl;

  private String redirectUrl;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public Applications(String name, String secretId) {

    this.secretId = (secretId == null || secretId.isEmpty()) ? newRandowSercretKey() : secretId;
    this.name = name;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public void setSecretId(String secretId) {

    if (secretId != null && secretId.length() < 88)
      throw new IllegalArgumentException("Secret inválida");

    this.secretId = secretId;
  }

  private String newRandowSercretKey() {

    byte[] bytes = new byte[64]; // 64 bytes = 512 bits
    new SecureRandom().nextBytes(bytes);
    return Base64.getEncoder().encodeToString(bytes);
  }

}
