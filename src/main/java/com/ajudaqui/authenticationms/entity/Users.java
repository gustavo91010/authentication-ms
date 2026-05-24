package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
@Document(collection = "users")
public class Users {

  @Id
  private String id;

  @NotBlank(message = "Campo nome não pode estar vazio")
  @Size(max = 100)
  private String name;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @Indexed(unique = true)
  @Size(max = 50)
  @NotBlank(message = "Campo email não pode estar vazio")
  private String email;

  private Set<UsersAppData> usersAppData = new HashSet<>();

  public Users(String name) {
    this.name = name;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

}
