package com.ajudaqui.authenticationms.service;

import static java.lang.String.format;

import java.util.Map;
import java.util.UUID;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.dto.ApplicationSqsMessage;
import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.sqs.SqsService;
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

  final private String ENVIROMENT_PROD = "prod";
  @Autowired
  private PageService pageService;
  @Value("${app.info.enviroment}")
  private String enviroment_current;

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

  /**
   * Realiza a autenticação do usuário com base no e-mail, senha e aplicação.
   *
   * <p>
   * Caso a aplicação não seja informada, define "bill-manager" como padrão.
   * Valida se o usuário está ativo, executa o processo de autenticação
   * pelo {@link AuthenticationManager} e, se bem-sucedido, armazena o
   * contexto de segurança e retorna o JWT correspondente.
   * </p>
   *
   * @param loginRequest dados de login contendo e-mail, senha e aplicação
   * @return {@link LoginResponse} com os dados do usuário na aplicação
   *         e o token JWT gerado
   * @throws MessageException caso a conta esteja desativada
   */

  public LoginResponse authenticateUser(LoginRequest loginRequest) {
    // TODO depois retirar essa validação
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
    String urlRegister = format("http://%s:8082/login/register-auth2", url);
    String urlLogin = format("redirect:http://%s:3000/?token=", url);
    String application = "bill-manager";
    if (usersAppDataService.findByUsersEmail(email, application).isPresent())
      return urlLogin + authenticateUser(email, application).getAccess_token();
    return pageService.showRegisterForm(application, urlLogin, urlRegister, email, name,
        modal);
  }

  /**
   * Realiza o registro de um novo usuário na aplicação.
   *
   * <p>
   * Cria o usuário, gera um token de confirmação e envia por e-mail.
   * Em ambiente de desenvolvimento, ele vai como ativo automaticamente,
   * em produção, deve chamar o endpont confirmByToken passando o token para
   * ativar o usuario
   * Em produção, valida se a aplicação possui URL de registro configurada e,
   * caso o registro seja concluído, envia mensagem para fila (SQS) com os dados
   * adicionais.
   * </p>
   *
   * @param usersRegister objeto contendo os dados necessários para registro do
   *                      usuário,
   *                      incluindo campos adicionais utilizados na integração.
   * @return LoginResponse contendo os dados da aplicação do usuário e o JWT
   *         gerado para autenticação.
   * @throws BadRequestException caso esteja em produção e a aplicação não possua
   *                             URL de registro configurada.
   */
  public LoginResponse registerUser(UsersRegister usersRegister) {
    boolean isProd = ENVIROMENT_PROD.equals(enviroment_current);
    UsersAppData userApp = usersService.create(usersRegister, !isProd);
    Applications application = userApp.getApplications();

    try {

      String token = tokenService.createToken(userApp.getUsers().getId());
      emailService.sendEmail(userApp.getUsers().getEmail(),
          "Token de confirmação do registro", token);

      if (!isProd)
        confirmByToken(jwtUtils.generatedJwtToken(userApp), token);

      if (userApp.getId() != null && isProd) {

        Map<String, Object> payload = usersRegister.getOtherFields();
        payload.put("access_token", userApp.getAccessToken());
        ApplicationSqsMessage sqsMessage = new ApplicationSqsMessage(
            application.getRegisterUrl(),
            application.getName(),
            application.getSecretId(),
            payload);
        messageSqsFactor(sqsMessage);
      }

    } catch (Exception e) {
      e.printStackTrace();
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
    UsersAppData byEmail = usersAppDataService.findByUsersId(byToken.getId());
    if (byToken.getUserId() == byEmail.getId()) {
      byEmail.setActive(true);
      usersAppDataService.save(byEmail);
      tokenService.delete(token);
    }
    return byEmail.isActive();
  }

  private void messageSqsFactor(ApplicationSqsMessage application) {
    // // ApplicationSqsMessage lalala= new ApplicationSqsMessage(
    // // application.getRegisterUrl(),
    // // application.getName(),
    // // application.getSecretId(),
    // // payload
    // // );
    // JsonObject sqsUsers = new JsonObject();

    // sqsUsers.addProperty("url", application.getRegisterUrl());
    // sqsUsers.addProperty("authorization", application.getSecretId());
    // sqsUsers.addProperty("application", application.getName());

    // sqsUsers.add("payload", new Gson().toJsonTree(payload));

    // sqsService.sendMessage(application, sqsUsers.toString());
    sqsService.sendMessage(application);
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
