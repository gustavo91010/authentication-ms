package com.ajudaqui.authenticationms.service;

import java.nio.channels.Pipe.SourceChannel;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.config.security.service.UserDetailsImpl;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.exception.MesageException;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;

@Service
public class AuthService {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UsersService usersService;

  @Autowired
  private JwtUtils jwtUtils;

  public LoginResponse authenticateUser(LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generatedJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());

    Users user = usersService.findByEmail(loginRequest.getEmail());
    LoginResponse loginResponse = new LoginResponse();

    loginResponse.setEmail(userDetails.getUsername());
    loginResponse.setActive(userDetails.getActive());
    loginResponse.setJwt(jwt);
    loginResponse.setRoles(roles);
    loginResponse.setAccess_token(user.getAccess_token());

    return loginResponse;
  }

  public Users registerUser(UsersRegister usersRegister) {

    // Verificando se usuario já possue registro
    if (usersService.existsByEmail(usersRegister.getEmail())) {
      throw new MesageException("Usuário já cadastrado.");
    }
    return usersService.create(usersRegister);
  }

  public boolean verifyToken(String accessToken) {
    Users users = usersService.findByAccessToken(accessToken);
    return users.getActive();
  }
}
