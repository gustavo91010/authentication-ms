package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotBlank;

import com.ajudaqui.authenticationms.entity.Users;

public class UsersRegister {


  @NotBlank(message = "Campo email é obrigatorio")
	private String email;
  @NotBlank(message = "Campo password é obrigatorio")
	private String password;
  @NotBlank(message = "Campo aplication é obrigatorio")
	private String aplication;

	public Users toDate() {
		
		String password = new BCryptPasswordEncoder().encode(this.password);
		
		Users user = new Users(this.email,  password, this.aplication);
		return user;
	}

	public String getEmail() {
		return email;
	}


	public String getPassword() {
		return password;
	}


	public String getAplication() {
		return aplication;
	}


}
