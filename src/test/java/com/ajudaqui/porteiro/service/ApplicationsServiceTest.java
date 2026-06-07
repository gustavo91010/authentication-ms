package com.ajudaqui.porteiro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.*;

import com.ajudaqui.porteiro.dto.ApplicationDto;
import com.ajudaqui.porteiro.entity.Applications;
import com.ajudaqui.porteiro.entity.Roles;
import com.ajudaqui.porteiro.entity.Users;
import com.ajudaqui.porteiro.entity.UsersAppData;
import com.ajudaqui.porteiro.exception.MessageException;
import com.ajudaqui.porteiro.repository.ApplicationsRepository;
import com.ajudaqui.porteiro.utils.enuns.ERoles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ApplicationsServiceTest {
  @Mock
  private ApplicationsRepository applicationsRepository;
  @Mock
  private UsersService usersService;
  @InjectMocks
  private ApplicationsService applicationsService;

  private final String AUTH_MASTER = "master-key";

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(applicationsService, "auth_master", AUTH_MASTER);
  }

  @Test
  void deveBuscarAplciacaoPeloNome() {
    String appName = "test-app";
    Applications app = new Applications();
    app.setName(appName);
    when(applicationsRepository.findByName(appName)).thenReturn(Optional.of(app));

    Applications result = applicationsService.findByName(appName);

    assertNotNull(result);
    assertEquals(appName, result.getName());
  }

  @Test
  void registerWithSuccess() {
    // Ambiente
    String authorization = AUTH_MASTER;
    ApplicationDto applicationDto = new ApplicationDto();
    applicationDto.setName("NewApp");
    applicationDto.setAppIdOfModerador("OldAppId");
    applicationDto.setEmailModerador("moderator@test.com");

    Users moderator = new Users();
    moderator.setEmail("moderator@test.com");
    moderator.setUsersAppData(new HashSet<>());

    UsersAppData moderatorOldAppData = new UsersAppData();
    moderatorOldAppData.setAppId("OldAppId");
    moderatorOldAppData.setPassword("encoded-password");
    moderator.getUsersAppData().add(moderatorOldAppData);

    Roles roleUser = new Roles();
    roleUser.setName(ERoles.ROLE_USER);
    Set<Roles> roles = new HashSet<>();
    roles.add(roleUser);

    Roles roleModerator = new Roles();
    roleModerator.setName(ERoles.ROLE_MODERATOR);
    Roles roleAdmin = new Roles();
    roleAdmin.setName(ERoles.ROLE_ADMIN);

    when(applicationsRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(usersService.findByEmail(applicationDto.getEmailModerador(), applicationDto.getAppIdOfModerador()))
        .thenReturn(Optional.of(moderator));

    when(applicationsRepository.save(any(Applications.class))).thenAnswer(invocation -> {
      Applications app = invocation.getArgument(0);
      app.setId("new-app-id");
      return app;
    });
    
    when(usersService.assignRole(ERoles.ROLE_USER)).thenReturn(new HashSet<>(roles));
    when(usersService.findByRole(ERoles.ROLE_MODERATOR)).thenReturn(roleModerator);

    // Mock para a parte do Admin opcional
    when(usersService.findByEmail(eq("admin@ajudaqui.com"), anyString())).thenReturn(Optional.empty());

    // Execução
    Applications result = applicationsService.register(authorization, applicationDto);

    // Verificação
    assertNotNull(result);
    verify(usersService, atLeastOnce()).update(any(Users.class));
    assertEquals("newapp", result.getName()); // O serviço converte para lowercase
  }

  @Test
  void test_003_registerShouldThrowExceptionWhenNameIsEmpty() {
    ApplicationDto dto = new ApplicationDto();
    dto.setName("");

    MessageException exception = assertThrows(MessageException.class, () -> {
      applicationsService.register(AUTH_MASTER, dto);
    });

    assertEquals("O campo name não pode estar vazio.", exception.getMessage());
  }

  @Test
  void test_004_registerShouldThrowExceptionWhenApplicationOfModeradorIsEmpty() {
    ApplicationDto dto = new ApplicationDto();
    dto.setName("newapp");
    dto.setAppIdOfModerador("");

    MessageException exception = assertThrows(MessageException.class, () -> {
      applicationsService.register(AUTH_MASTER, dto);
    });

    assertEquals("O campo appId do moderador não pode estar vazio.", exception.getMessage());
  }

  @Test
  void test_005_registerShouldThrowExceptionWhenEmailModeradorIsEmpty() {
    ApplicationDto dto = new ApplicationDto();
    dto.setName("newapp");
    dto.setAppIdOfModerador("oldapp");
    dto.setEmailModerador("");

    MessageException exception = assertThrows(MessageException.class, () -> {
      applicationsService.register(AUTH_MASTER, dto);
    });

    assertEquals("O campo email do emailModerador não pode estar vazio.", exception.getMessage());
  }
}
