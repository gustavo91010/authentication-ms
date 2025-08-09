package com.ajudaqui.authenticationms.config.security.service;

import javax.transaction.Transactional;

import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.repository.UsersAppDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UsersAppDataRepository usersRepository;

  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UsersAppData user = usersRepository.findByUserEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}
