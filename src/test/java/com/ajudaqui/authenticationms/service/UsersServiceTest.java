package com.ajudaqui.authenticationms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.ApplicationsRepository;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

  @Mock
  private UsersRepository userRepository;
  @Mock
  private ApplicationsRepository applicationsService;
  @Mock
  private RolesRepository rolesRepository;
  @InjectMocks
  private UsersService usersService;

  @Test
  void deveLancarExceptionSeAplicacaoNaoregsitrada() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();

    // verificação:
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      usersService.create(usersRegister);
    });

    String message = exception.getMessage();
    // Verificação
    assertEquals("Aplicação nao registrada", message);
  }

  @Test
  void deveLancarExceptionSeAplicacaoNaoTiverUrlRegistro() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setAppId("app-api");

    Applications app = new Applications();
    when(applicationsService.findById(usersRegister.getAppId()))
        .thenReturn(Optional.of(app));

    // verificação:
    BadRequestException exception = assertThrows(BadRequestException.class, () -> {
      usersService.create(usersRegister);
    });

    String message = exception.getMessage();
    // Verificação
    assertEquals("A Aplicação não tem URL de registro cadastrada", message);
  }

  @Test
  void deveLancarExceptionSeEmailJaRegsitrado() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setAppId("app-api");
    usersRegister.setEmail("email@email.com");

    Applications app = new Applications();
    app.setRegisterUrl("usersRegister");

    // Mockando resultados
    when(applicationsService.findById(usersRegister.getAppId()))
        .thenReturn(Optional.of(app));
    when(userRepository
        .findByEmailAndUsersAppDataAppId(
            usersRegister.getEmail(),
            usersRegister.getAppId()))
        .thenReturn(Optional.of(new Users()));

    // verificação:
    MessageException exception = assertThrows(MessageException.class, () -> {
      usersService.create(usersRegister);
    });

    String message = exception.getMessage();
    // Verificação
    assertEquals("Email já registrado", message);
  }

  @Test
  void develancarExceptionSeNaoEncontrarRole() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setAppId("app-api");
    usersRegister.setEmail("email@email.com");

    Applications app = new Applications();
    app.setRegisterUrl("usersRegister");

    // Mockando resultados
    when(applicationsService.findById(usersRegister.getAppId()))
        .thenReturn(Optional.of(app));

    // verificação:
    MessageException exception = assertThrows(MessageException.class, () -> {
      usersService.create(usersRegister);
    });

    String message = exception.getMessage();
    // Verificação
    assertEquals("Roles não encontrado.", message);
  }

  @Test
  void deveCriarUserNoActiveSeTiverUrlLoginByToken() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setAppId("app-api");
    usersRegister.setEmail("email@email.com");
    usersRegister.setPassword("@Ajudaqui");

    Applications app = new Applications();
    app.setRegisterUrl("usersRegister");
    app.setLoginByTokenUrl("loginByTokenUrl");

    // Mockando resultados
    when(applicationsService.findById(usersRegister.getAppId()))
        .thenReturn(Optional.of(app));

    when(rolesRepository.findByName(ERoles.ROLE_USER))
        .thenReturn(Optional.of(new Roles()));

    // Execução:
    Users users = usersService.create(usersRegister);

    // verificação
    assertTrue(!app.getLoginByTokenUrl().isEmpty());
    assertFalse(users.selectApp(usersRegister.getAppId()).isActive());

  }

  @Test
  void deveCriarUserData() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setAppId("app-api");
    usersRegister.setEmail("email@email.com");
    usersRegister.setPassword("@Ajudaqui");

    Applications app = new Applications();
    app.setRegisterUrl("usersRegister");

    // Mockando resultados
    when(applicationsService.findById(usersRegister.getAppId()))
        .thenReturn(Optional.of(app));

    when(rolesRepository.findByName(ERoles.ROLE_USER))
        .thenReturn(Optional.of(new Roles()));

    // Execução:
    Users users = usersService.create(usersRegister);

    // verificação
    assertEquals(1, users.getUsersAppData().size());
    assertNotNull(users.getCreatedAt());
    assertNull(app.getLoginByTokenUrl());
    assertTrue(users.selectApp(usersRegister.getAppId()).isActive());

  }

  void deveIncluirUserData() {

  }

}
