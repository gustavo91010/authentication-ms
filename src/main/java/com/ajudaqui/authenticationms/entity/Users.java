package com.ajudaqui.authenticationms.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Campo email não pode estar vazio")
	@Size(max = 50)
	private String email;

	@NotBlank(message = "Campo password não pode estar vazio")
	@Size(max = 120)
	private String password;
	@NotNull
	private Boolean active;

	private LocalDateTime created_at;

	private LocalDateTime update_at;
	private String access_token;
	private String aplication;
	public Users() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Users(String email, String password, String aplication) {
		this.email = email;
		this.password = password;
		this.aplication= aplication;
		this.active= true;
		this.created_at= LocalDateTime.now();
		this.update_at= LocalDateTime.now();

		this.access_token= UUID.randomUUID().toString();
		
	}

	public String getAplication() {
		return aplication;
	}

	public void setAplication(String aplication) {
		this.aplication = aplication;
	}

	// @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
	// inverseJoinColumns = @JoinColumn(name = "role_id"))
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Roles> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdate_at() {
		return update_at;
	}

	public void setUpdate_at(LocalDateTime update_at) {
		this.update_at = update_at;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	@Override
	public String toString() {
		return "Users [id=" + id + ", email=" + email + ", password=" + password + ", active=" + active
				+ ", created_at=" + created_at + ", update_at=" + update_at + ", access_token=" + access_token
				+ ", aplication=" + aplication + ", roles=" + roles + "]";
	}

}
