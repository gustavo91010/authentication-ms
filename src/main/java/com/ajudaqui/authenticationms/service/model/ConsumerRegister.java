package com.ajudaqui.authenticationms.service.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ajudaqui.authenticationms.entity.Consumer;

public class ConsumerRegister {

	private String login;
	private String password;

	public Consumer toDate() {

		String password = new BCryptPasswordEncoder().encode(this.password);

		return new Consumer(this.login, password);

	}



}
