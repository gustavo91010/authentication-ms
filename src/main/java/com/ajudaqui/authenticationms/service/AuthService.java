package com.ajudaqui.authenticationms.service;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.Token;
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
import org.springframework.ui.Model;

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
    UsersAppData user = usersAppDataService.findByUsersEmail(loginRequest.getEmail())
        .orElseThrow(() -> new MessageException("Usuário não localizado"));
    if (!user.isActive())
      throw new MessageException("sua conta esta desativada, por favor, entre em contato...");

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UsersAppApplicationDto userAplication = new UsersAppApplicationDto(user);
    String jwt = jwtUtils.generatedJwtToken(userAplication);
    return new LoginResponse(userAplication, jwt);
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
    if (usersAppDataService.findByUsersEmail(email).isPresent())
      return urlLogin + authenticateUser(email).getAccess_token();
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
    // TODO só libera isso depois de ter corrigido os regsitros em producao
    // if (users.getUserId() != null && ENVIROMENT.equals(enviriment))
    //   messageSqsFactor(users);

    return new LoginResponse(users, jwtUtils.generatedJwtToken(users));
  }

  public Boolean confirmByToken(String email, String token) {
    Token byToken = tokenService.findByToken(token);
    UsersAppData byEmail = usersAppDataService.findByUsersEmail(email)
        .orElseThrow(() -> new MessageException("Usuário não localizado"));
    if (byToken.getUserId() == byEmail.getId()) {
      byEmail.setActive(true);
      usersAppDataService.save(byEmail);
      tokenService.delete(token);
    }
    return byEmail.isActive();
  }

  private void messageSqsFactor(UsersAppApplicationDto users) {
    String application = users.getAppName();
    JsonObject sqsUsers = new JsonObject();
    sqsUsers.addProperty("access_token", users.getAccessTokne().toString());
    sqsUsers.addProperty("application", application);
    sqsService.sendMessage(application, sqsUsers.toString());
  }

  public boolean verifyToken(String accessToken) {
    return usersAppDataService.findByAccessToken(accessToken).isActive();
  }
}
