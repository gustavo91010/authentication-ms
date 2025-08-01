package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "users_email")
public class UsersEmail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private Users user;

  @Column(name = "email", nullable = false, length = 150)
  private String email;

  @Column(name = "is_verified")
  private Boolean isVerified = false;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

}
