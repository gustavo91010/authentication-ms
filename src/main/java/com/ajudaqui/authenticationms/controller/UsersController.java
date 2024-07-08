package com.ajudaqui.authenticationms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajudaqui.authenticationms.entity.Client;
import com.ajudaqui.authenticationms.service.ClientService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private ClientService usersService;
	
	@GetMapping("/{id}")
	public Client findById(@PathVariable Long id) {
		return usersService.findById(id);
	}
	
	@GetMapping()
	@PreAuthorize("hasRole('MODERATOR')")
	public List<Client> findAll(@RequestHeader("Authorization") String jwtToken) {
		return usersService.findAll();
	}
	@GetMapping("/vei")
	public List<Client> opa() {
		System.err.println("vei!??");
		return usersService.findAll();
	}

}
