package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Campo nome não pode estar vazio")
  @Size(max = 100)
  private String name;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UsersEmail> emails;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "update_at")
  private LocalDateTime updatedAt;

  public Users() {
  }

  public Users(String name) {
    this.name = name;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}
