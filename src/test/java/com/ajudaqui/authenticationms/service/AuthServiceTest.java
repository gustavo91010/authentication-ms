// package com.ajudaqui.authenticationms.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.when;

// import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
// import com.ajudaqui.authenticationms.entity.Applications;
// import com.ajudaqui.authenticationms.entity.Users;
// import com.ajudaqui.authenticationms.entity.UsersAppData;
// import com.ajudaqui.authenticationms.request.UsersRegister;
// import com.ajudaqui.authenticationms.response.LoginResponse;
// import com.ajudaqui.authenticationms.service.sqs.SqsService;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.security.authentication.AuthenticationManager;

// @ExtendWith(MockitoExtension.class)
// public class AuthServiceTest {

//   @Mock
//   private AuthenticationManager authenticationManager;
//   @Mock
//   private SqsService sqsService;
//   @Mock
//   private UsersService usersService;
//   @Mock
//   private JwtUtils jwtUtils;
//   @Mock
//   private TokenService tokenService;
//   @Mock
//   private EmailService emailService;
//   @Mock
//   private UsersAppDataService usersAppDataService;

//   @InjectMocks
//   private AuthService authService;

//   @Test
//   void deveRegistrarComSucesso() {
//     // Ambiente
//     UsersRegister usersRegister = new UsersRegister();
//     usersRegister.setName("Registro test da silva");
//     usersRegister.setEmail("registro_test@email.com");
//     usersRegister.setPassword("@Ajudaqui");
//     usersRegister.setAppId("authentication-ms");

//     UsersAppData userApp = new UsersAppData();
//     Users user = new Users();
//     user.setId(10L);
//     user.setEmail("registro_test@email.com");
//     userApp.setId(1L);
//     userApp.setUsers(user);

//     Applications app = new Applications();
//     app.setName("authentication-ms");
//     userApp.setApplications(app);

//     String jwtToken = "jwt-token";
//     when(usersService.create(any(UsersRegister.class), anyBoolean())).thenReturn(userApp);
//     when(tokenService.createToken(any())).thenReturn("token-123");
//     when(jwtUtils.generatedJwtToken(any(UsersAppData.class))).thenReturn(jwtToken);

//     // Execução
//     LoginResponse registered = authService.registerUser(usersRegister);

//     // Verificação:
//     assertNotNull(registered);
//     assertEquals(usersRegister.getEmail(), registered.getEmail());
//     assertEquals(user.getEmail(), registered.getEmail());
//     // assertEquals(userApp.getId(), registered.getId());
//     assertEquals(jwtToken, registered.getJwt());
//     assertEquals(app.getName(), registered.getApplication());
//   }

// }
