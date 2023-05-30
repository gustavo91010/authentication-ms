package com.ajudaqui.authenticationms.response;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse {
	
	private Long id;
	private String username;
	private String email;
	private String jwt;
	private List<String> roles=new ArrayList<>();
	
	
	
	public String getJwt() {
		return jwt;
	}
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	

}
