package com.ajudaqui.authenticationms.request;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ajudaqui.authenticationms.entity.Users;

public class UsersRegister {


	private String username;
	private String password;
	private String aplication;

	public Users toDate() {
		
		String password = new BCryptPasswordEncoder().encode(this.password);
		
		Users user = new Users(this.username,  password, this.aplication);
		return user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
