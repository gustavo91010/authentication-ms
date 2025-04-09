package com.ajudaqui.authenticationms.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginRequest {
  @NotEmpty(message = "O campo email não pode ser vazio")
  @NotNull(message = "ha!")
	private String email;
  @NotNull(message = "ha!")
	private String password;
	
	
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
	@Override
	public String toString() {
		return "LoginRequest [email=" + email + ", password=" + password + "]";
	}
	
	

}
