package com.ajudaqui.authenticationms.config.security.service;

import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UsersRepository usersRepository;

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

    Users user = usersRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User Not Found with email: " + email);
    }

    // final String finalApplication = application;
    // UsersAppData userApp = user.getUsersAppData().stream()
    //     .filter(u -> {
    //       if (finalApplication != null) {
    //         return finalApplication.equals(u.getAppId());
    //       }
    //       // Se não especificou app, procura um onde ele seja MODERADOR (para o Admin Dashboard)
    //       return u.getRoles().stream().anyMatch(r -> r.getName().name().equals("ROLE_MODERATOR"));
    //     })
    //     .findFirst()
    //     .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}
