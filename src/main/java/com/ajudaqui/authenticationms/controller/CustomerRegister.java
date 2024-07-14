package com.ajudaqui.authenticationms.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ajudaqui.authenticationms.entity.Consumer;

public class CustomerRegister {
	private String username;
	private String password;

	public Consumer toDate() {

		String password = new BCryptPasswordEncoder().encode(this.password);

		Consumer user = new Consumer(this.username, password);
		return user;
	}

	public String getUsername() {
		return username;
	}

}
