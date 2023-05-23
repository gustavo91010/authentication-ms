package com.ajudaqui.authenticationms.security.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.models.Users;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UsersRepository usersRepository;
	
	
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = usersRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
	}

}
