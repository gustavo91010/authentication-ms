package com.ajudaqui.authenticationms.service;

import static java.lang.String.format;

import java.util.UUID;

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
import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Retry;
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

  @Value("${app.url}")
  private String url;

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
    if (loginRequest.getApplication() == null)
      loginRequest.setApplication("bill-manager");

    UsersAppData usersApp = usersAppDataService.getUsersByEmail(loginRequest.getEmail(), loginRequest.getApplication());
    if (!usersApp.isActive())
      throw new MessageException("sua conta esta desativada, verifique seu email");
    UsernamePasswordAuthenticationToken userAutheticator = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail(),
        loginRequest.getPassword());

    Authentication authentication = authenticationManager.authenticate(userAutheticator);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    return new LoginResponse(new UsersAppApplicationDto(usersApp),
        jwtUtils.generatedJwtToken(usersApp));
  }

  public LoginResponse authenticateUser(String email, String application) {
    UsersAppData usersApp = usersAppDataService.getUsersByEmail(email, application);
    return new LoginResponse(new UsersAppApplicationDto(usersApp),
        jwtUtils.generatedJwtToken(usersApp));
  }

  public String authOrRegister(String email, String name, Model modal) {
    String urlLogin = format("redirect:http://%s:3000/?token=", url);
    String application = "bill-manager";
    // http://localhost:3000/api/auth/callback?token=JWT_AQUI&name=Sr-lalala&email=email-test@lalala.com

    if (usersAppDataService.findByUsersEmail(email, application).isPresent()) {
      LoginResponse login = authenticateUser(email, application);
      return autoLogin(login.getJwt(), login.getName(), login.getEmail());
      // return
      // String.format("http://localhost:3000/api/auth/callback?token=%s&name=%s&email=%s",
      // login.getJwt(), login.getName(), login.getEmail());

      // return urlLogin + authenticateUser(email, application).getAccess_token();
    }
    String urlRegister = format("http://%s:8082/login/register-auth2", url);
    return pageService.showRegisterForm(application, urlLogin, urlRegister, email, name,
        modal);
  }

  public String autoLogin(String jwt, String name, String email) {

    return String.format("http://%s:3000/api/auth/callback?token=%s&name=%s&email=%s",
        url, jwt, name, email);
  }

  public LoginResponse registerUser(UsersRegister usersRegister) {
    boolean isInternal = !ENVIROMENT.equals(enviriment);
    UsersAppData usersApp = usersService.create(usersRegister, isInternal);
    String tokenTemporario = "";
    if (!isInternal) {
      try {

        String token = tokenService.createToken(usersApp.getUsers().getId());
        emailService.sendEmail(usersApp.getUsers().getEmail(), "Token de confirmação do registro",
            token);
        if (usersApp.getId() != null && ENVIROMENT.equals(enviriment))
          messageSqsFactor(usersApp);
      } catch (Exception e) {
      }
    }
    // LoginResponse login = new LoginResponse(new UsersAppApplicationDto(usersApp),
    // jwtUtils.generatedJwtToken(usersApp));
    // if (!isInternal)
    // confirmByToken(login.getJwt(), tokenTemporario);
    return new LoginResponse(new UsersAppApplicationDto(usersApp), jwtUtils.generatedJwtToken(usersApp));
  }

  public Boolean confirmByToken(String jwtToken, String token) {
    Token byToken = tokenService.findByToken(token);
    UsersAppData byEmail = usersAppDataService.findByUsersId(byToken.getId());
    if (byToken.getUserId() == byEmail.getId()) {
      byEmail.setActive(true);
      usersAppDataService.save(byEmail);
      tokenService.delete(token);
    }
    return byEmail.isActive();
  }

  private void messageSqsFactor(UsersAppData userApp) {
    String application = userApp.getApplications().getName();
    JsonObject sqsUsers = new JsonObject();
    sqsUsers.addProperty("access_token", userApp.getAccessToken().toString());
    sqsUsers.addProperty("application", application);
    sqsUsers.addProperty("email", userApp.getUsers().getEmail());
    sqsService.sendMessage(application, sqsUsers.toString());
  }

  public boolean verifyToken(String accessToken) {
    return usersAppDataService.findByAccessToken(UUID.fromString(accessToken)).isActive();
  }
}
