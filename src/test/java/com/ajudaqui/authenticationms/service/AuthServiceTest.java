package com.ajudaqui.authenticationms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.sqs.SqsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private AuthenticationManager authenticationManager;
  @Mock
  private SqsService sqsService;
  @Mock
  private UsersService usersService;
  @Mock
  private JwtUtils jwtUtils;
  @Mock
  private TokenService tokenService;
  @Mock
  private EmailService emailService;

  @InjectMocks
  private AuthService authService;

  @Test
  void deveRegistrarComSucesso() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setName("Registro test da silva");
    usersRegister.setEmail("registro_test@email.com");
    usersRegister.setPassword("@Ajudaqui");
    usersRegister.setAppId("app-id-authentication");

    Applications app = new Applications();
    app.setName("authentication-ms");
    app.setId(usersRegister.getAppId());

    Users user = new Users();
    user.setId("10L");
    user.setEmail("registro_test@email.com");

    UsersAppData userApp = new UsersAppData();
    userApp.setAppId(app.getId());
    userApp.setActive(false);

    user.getUsersAppData().add(userApp);

    String jwtToken = "jwt-token";
    when(usersService.create(any(UsersRegister.class))).thenReturn(user);
    when(jwtUtils.generatedJwtToken(any(UsersAppData.class))).thenReturn(jwtToken);

    // Execução
    LoginResponse registered = authService.registerUser(usersRegister);

    // Verificação:
    assertNotNull(registered);
    assertEquals(usersRegister.getEmail(), registered.getEmail());
    assertEquals(user.getEmail(), registered.getEmail());
    assertEquals(jwtToken, registered.getJwt());
  }

}
