package com.ajudaqui.authenticationms.config.security.service;

import jakarta.transaction.Transactional;

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
    String email;
    String application = null;

    if (username.contains("|")) {
      String[] parts = username.split("\\|");
      email = parts[0];
      application = parts[1];
    } else {
      email = username;
    }

    final String finalApplication = application;
    UsersAppData user = usersRepository.findByUserEmail(email).stream()
        .filter(u -> {
          if (finalApplication != null) {
            return finalApplication.equals(u.getApplications().getName());
          }
          // Se não especificou app, procura um onde ele seja MODERADOR (para o Admin Dashboard)
          return u.getRoles().stream().anyMatch(r -> r.getName().name().equals("ROLE_MODERATOR"));
        })
        .findFirst()
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}
