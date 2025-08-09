package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "user_app_data")
public class UsersAppData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @JoinColumn(name = "user_id")
  @ManyToOne // um usuair opode rer maus de um app data
  private Users users;

  @JoinColumn(name = "app_id")
  @ManyToOne // um usuair opode rer maus de um app data
  private Applications applications;

  @Column(name = "password", length = 255)
  private String password;

  @Column(name = "auth_provider", length = 50)
  private String authProvider = "email"; // metodo de login por aplicação

  @Column(name = "provider_id", length = 255) // Id retonado pelo provedor, no caso de auth2
  private String providerId;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "is_active")
  private boolean isActive;

  @Column(name = "access_token")
  private UUID accessToken;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_app_data_roles", joinColumns = @JoinColumn(name = "user_app_data_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Roles> roles = new HashSet<>();

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(String authProvider) {
    this.authProvider = authProvider;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
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

  public Set<Roles> getRoles() {
    return roles;
  }

  public void setRoles(Set<Roles> roles) {
    this.roles = roles;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public UUID getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(UUID accessToken) {
    this.accessToken = accessToken;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Users getUsers() {
    return users;
  }

  public void setUsers(Users users) {
    this.users = users;
  }

  public Applications getApplications() {
    return applications;
  }

  public void setApplications(Applications applications) {
    this.applications = applications;
  }

}
