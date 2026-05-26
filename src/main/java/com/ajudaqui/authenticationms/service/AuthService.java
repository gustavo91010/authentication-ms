package com.ajudaqui.authenticationms.service;

import static java.lang.String.format;

import java.util.Map;
import java.util.UUID;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.dto.ApplicationSqsMessage;
import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.doc.AuthServiceDoc;
import com.ajudaqui.authenticationms.service.sqs.SqsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AuthService implements AuthServiceDoc {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  final private String ENVIROMENT_PROD = "prod";
  @Autowired
  private PageService pageService;
  @Value("${app.info.enviroment}")
  private String enviroment_current;

  @Value("${app.url}")
  private String url;

  @Value("${app.auth.master}")
  private String auth_master;

  private AuthenticationManager authenticationManager;
  private EmailService emailService;
  private SqsService sqsService;
  private UsersService usersService;
  private JwtUtils jwtUtils;
  final private TokenService tokenService;

  public AuthService(AuthenticationManager authenticationManager, SqsService sqsService, UsersService usersService,
      JwtUtils jwtUtils, TokenService tokenService, EmailService emailService) {
    this.authenticationManager = authenticationManager;
    this.sqsService = sqsService;
    this.emailService = emailService;
    this.usersService = usersService;
    this.jwtUtils = jwtUtils;
    this.tokenService = tokenService;
  }

  @Override
  public LoginResponse authenticateUser(LoginRequest loginRequest) {

    Users users = usersService.findByEmail(loginRequest.getEmail());
    UsersAppData usersApp = users.selectApp(loginRequest.getApplication());
    if (!usersApp.isActive())
      throw new MessageException("sua conta esta desativada, verifique seu email");
    UsernamePasswordAuthenticationToken userAutheticator = new UsernamePasswordAuthenticationToken(
        loginRequest.getEmail() + "|" + usersApp.getAppName(), // Poderia passar o accessToken aqui, ja que ele é unico por aplicação
        loginRequest.getPassword());

    Authentication authentication = authenticationManager.authenticate(userAutheticator);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new LoginResponse(new UsersAppApplicationDto(usersApp),
        jwtUtils.generatedJwtToken(usersApp));
  }

  public LoginResponse authenticateUser(String email, String application) {
    UsersAppData usersApp = usersService.getUsersByEmail(email, application);
    return new LoginResponse(new UsersAppApplicationDto(usersApp),
        jwtUtils.generatedJwtToken(usersApp));
  }

  public String authOrRegister(String email, String name, Model modal) {
    String urlRegister = format("http://%s:8082/login/register-auth2", url);
    String urlLogin = format("redirect:http://%s:3000/?token=", url);
    String application = "bill-manager";
    Users byEmail = usersService.findByEmail(email);

    if (byEmail.selectApp(name) != null)
      return urlLogin + authenticateUser(email, application).getAccess_token();
    return pageService.showRegisterForm(application, urlLogin, urlRegister, email, name,
        modal);
  }

  @Override
  public LoginResponse registerUser(UsersRegister usersRegister) {
    boolean isProd = ENVIROMENT_PROD.equals(enviroment_current);
    UsersAppData userApp = usersService.create(usersRegister, !isProd);
    Applications application = userApp.getApplications();

    try {

      String token = tokenService.createToken(userApp.getUsers().getId());
      emailService.sendEmail(userApp.getUsers().getEmail(),
          "Token de confirmação do registro", token);

      if (!isProd) // nao estou pedindo isso em produção ainda.
        confirmByToken(jwtUtils.generatedJwtToken(userApp), token);

      if (userApp.getId() != null && isProd) {

        Map<String, Object> payload = usersRegister.getPayload();
        payload.put("access_token", userApp.getAccessToken());

        ApplicationSqsMessage sqsMessage = new ApplicationSqsMessage(
            application.getRegisterUrl(),
            application.getName(),
            application.getSecretId(),
            payload);
        messageSqsFactor(sqsMessage);
      }

    } catch (Exception e) {
      logger.error("Erro no envio da mensagem para fila sqs", e);
    }

    return new LoginResponse(new UsersAppApplicationDto(userApp), jwtUtils.generatedJwtToken(userApp));
  }

  /**
   * Confirma o registro de um usuário a partir do token informado. que é recebido
   * pelo email registrado
   *
   * <p>
   * Valida o token, localiza o usuário associado e,
   * caso o identificador corresponda, ativa o registro,
   * persiste a alteração e remove o token utilizado.
   * </p>
   *
   * @param jwtToken token JWT gerado para o usuário (não utilizado na validação
   *                 atual)
   * @param token    token de confirmação enviado ao usuário via email
   * @return {@code true} se o usuário estiver ativo após o processo,
   *         {@code false} caso contrário
   */
  public Boolean confirmByToken(String jwtToken, String token) {
    Token byToken = tokenService.findByToken(token);
    if (byToken == null) {
      return false;
    }
    String email = jwtUtils.getEmailFromJwtToken(jwtToken);
    String application = jwtUtils.getAppFromJwtToken(jwtToken);
    UsersAppData usersAppData = usersAppDataService.getUsersByEmail(email, application);

    if (byToken.getUserId().equals(usersAppData.getUsers().getId())) {
      usersAppData.setActive(true);
      usersAppDataService.save(usersAppData);
      tokenService.delete(token);
    }
    return usersAppData.isActive();
  }

  private void messageSqsFactor(ApplicationSqsMessage application) {
    sqsService.sendMessage(auth_master, application);
  }

  /**
   * Verifica se o token de acesso informado pertence a um usuário ativo.
   *
   * <p>
   * Converte o {@code accessToken} para {@link UUID}, busca o registro
   * correspondente e retorna o status de ativação.
   * </p>
   *
   * @param accessToken token de acesso no formato String
   * @return {@code true} se o usuário associado estiver ativo,
   *         {@code false} caso contrário
   * @throws IllegalArgumentException caso o token não esteja em formato UUID
   *                                  válido
   */
  public boolean verifyToken(String accessToken) {
    return usersAppDataService.findByAccessToken(UUID.fromString(accessToken)).isActive();
  }
}
