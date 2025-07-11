package com.ajudaqui.authenticationms.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.config.security.service.UserDetailsImpl;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.sqs.SqsService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private SqsService sqsService;

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
    LoginResponse loginResponse = new LoginResponse(user, roles, jwt);


    return new LoginResponse(user, roles, jwt);
  }

  public Users registerUser(UsersRegister usersRegister) {

    // Verificando se usuario já possue registro
    if (usersService.existsByEmail(usersRegister.getEmail())) {
      throw new MessageException("Usuário já cadastrado.");
    }
    Users users = usersService.create(usersRegister);
    if (users.getId() != null) {
      messageSqsFactor(users);
    }
    return users;

  }

  private void messageSqsFactor(Users users) {

    String application = users.getAplication();
    JsonObject sqsUsers = new JsonObject();
    sqsUsers.addProperty("access_token", users.getAccess_token());
    sqsUsers.addProperty("application", application);
    sqsService.sendMessage(application, sqsUsers.toString());

  }

  public boolean verifyToken(String accessToken) {
    Users users = usersService.findByAccessToken(accessToken);
    return users.getActive();
  }
}
