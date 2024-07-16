package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ajudaqui.authenticationms.entity.Users;

public class UsersRegister {


	private String email;
	private String password;
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
