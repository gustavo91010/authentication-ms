package com.ajudaqui.authenticationms.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Entity

public class Roles {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private ERoles name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ERoles getName() {
		return name;
	}

	public void setName(ERoles name) {
		this.name = name;
	}
	
	

}
