package com.ajudaqui.authenticationms.config.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ajudaqui.authenticationms.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;
  private Boolean active;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String username, String password, Boolean active,
      Collection<? extends GrantedAuthority> authorities) {
    super();
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.active = active;
  }

  public static UserDetailsImpl build(Users users) {

    // List<GrantedAuthority> authorites = users.getRoles().stream()
    // .map(role -> new
    // SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

    // return new UserDetailsImpl(users.getId(), users.getEmail(),
    // users.getPassword(), users.getActive(), authorites);
    return null;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getActive() {
    return active;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Auto-generated method stub
    return authorities;
  }

  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return password;
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorities, id, password, username);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserDetailsImpl other = (UserDetailsImpl) obj;
    return Objects.equals(authorities, other.authorities) && Objects.equals(id, other.id)
        && Objects.equals(password, other.password) && Objects.equals(username, other.username);
  }

}
