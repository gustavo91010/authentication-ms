package com.ajudaqui.authenticationms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.service.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
	
	@Autowired
	private UsersService usersService;
	
	@GetMapping("/{id}")
	public Users findById(@PathVariable Long id) {
		return usersService.findById(id);
	}
	
	@GetMapping()
	@PreAuthorize("hasRole('MODERATOR')")
	public List<Users> findAll(@RequestHeader("Authorization") String jwtToken) {
		System.err.println("chamou??");
		return usersService.findAll();
	}
	@GetMapping("/opa")
	public List<Users> opa() {
		System.err.println("opa!??");
		return usersService.findAll();
	}

}
