package com.ajudaqui.authenticationms.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.config.security.service.UserDetailsImpl;
import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.Token;
import com.ajudaqui.authenticationms.entity.Token;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.sqs.SqsService;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  final private String ENVIROMENT = "prod";
  @Autowired
  private PageService pageService;
  @Value("${app.info.enviroment}")
  private String enviriment;
  private AuthenticationManager authenticationManager;
  private UsersAppDataService usersAppDataService;
  private EmailService emailService;
  private SqsService sqsService;
  private UsersService usersService;
  private JwtUtils jwtUtils;
  final private TokenService tokenService;

  public AuthService(AuthenticationManager authenticationManager, SqsService sqsService, UsersService usersService,
      JwtUtils jwtUtils, TokenService tokenService, EmailService emailService,
      UsersAppDataService usersAppDataService) {
    this.authenticationManager = authenticationManager;
    this.sqsService = sqsService;
    this.usersAppDataService = usersAppDataService;
    this.emailService = emailService;
    this.usersService = usersService;
    this.jwtUtils = jwtUtils;
    this.tokenService = tokenService;
  }

  public LoginResponse authenticateUser(LoginRequest loginRequest) {
    return null;
    // Users user = usersService.findByEmail(loginRequest.getEmail());
    // if (!user.getActive()) {
    // throw new MessageException("sua conta esta desativada, por favor, procure o
    // patrão...");
    // }
    // Authentication authentication = authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
    // loginRequest.getPassword()));

    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // String jwt = jwtUtils.generatedJwtToken(user);
    // UserDetailsImpl userDetails = (UserDetailsImpl)
    // authentication.getPrincipal();

    // List<String> roles = userDetails.getAuthorities().stream().map(item ->
    // item.getAuthority())
    // .collect(Collectors.toList());
    // return new LoginResponse(user, roles, jwt);
  }

  public LoginResponse authenticateUser(String email) {
    UsersAppData usersApp = usersAppDataService.findByUsersEmail(email)
        .orElseThrow(() -> new MessageException("Usuário não regisrado"));
    UsersAppApplicationDto userAppDto = new UsersAppApplicationDto(usersApp);
    return new LoginResponse(userAppDto, jwtUtils.generatedJwtToken(userAppDto));
  }

  public String authOrRegister(String email, String name, Model modal) {
    String urlRegister = "http://localhost:8082/login/register-auth2";
    String urlLogin = "redirect:http://localhost:3000/?token=";
    String application = "bill-manager";
    // if (usersService.existsByEmail(email))
    // return urlLogin + authenticateUser(email).getAccess_token();
    return pageService.showRegisterForm(application, urlLogin, urlRegister, email, name,
        modal);
  }

  public LoginResponse registerUser(UsersRegister usersRegister) {
    UsersAppApplicationDto users = usersService.create(usersRegister);
    if (!users.isActive() && ENVIROMENT.equals(enviriment)) {
      String token = tokenService.createToken(users.getUserId());
      emailService.sendEmail(users.getEmail(), "Token de confirmação do registro",
          token);
    }
    if (users.getUserId() != null && ENVIROMENT.equals(enviriment))
      messageSqsFactor(users);

    return new LoginResponse(users, jwtUtils.generatedJwtToken(users));
  }

  public Boolean confirmByToken(String email, String token) {
    return null;
    // Token byToken = tokenService.findByToken(token);
    // Users byEmail = usersService.findByEmail(email);
    // if (byToken.getUserId() == byEmail.getId()) {
    // byEmail.setActive(true);
    // usersService.update(byEmail);
    // tokenService.delete(token);
    // }
    // return byEmail.getActive();
  }

  private void messageSqsFactor(UsersAppApplicationDto users) {
    String application = users.getAppName();
    JsonObject sqsUsers = new JsonObject();
    sqsUsers.addProperty("access_token", users.getAccessTokne().toString());
    sqsUsers.addProperty("application", application);
    sqsService.sendMessage(application, sqsUsers.toString());
  }

  public boolean verifyToken(String accessToken) {
    return false;
    // return usersService.findByAccessToken(accessToken).getActive();
  }
}
