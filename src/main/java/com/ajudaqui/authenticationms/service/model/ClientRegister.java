package com.ajudaqui.authenticationms.service.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ajudaqui.authenticationms.entity.Client;

public class ClientRegister {

	private String username;
	private String password;

	public Client toDate() {

		String password = new BCryptPasswordEncoder().encode(this.password);

		Client user = new Client(this.username, password);
		return user;
	}

	public String getUsername() {
		return username;
	}

}
