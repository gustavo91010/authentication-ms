// package com.ajudaqui.authenticationms.service;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
// import static org.mockito.ArgumentMatchers.*;

// import java.util.*;

// import com.ajudaqui.authenticationms.dto.ApplicationDto;
// import com.ajudaqui.authenticationms.entity.Applications;
// import com.ajudaqui.authenticationms.entity.Roles;
// import com.ajudaqui.authenticationms.entity.Users;
// import com.ajudaqui.authenticationms.entity.UsersAppData;
// import com.ajudaqui.authenticationms.exception.MessageException;
// import com.ajudaqui.authenticationms.repository.ApplicationsRepository;
// import com.ajudaqui.authenticationms.utils.enuns.ERoles;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.test.util.ReflectionTestUtils;

// @ExtendWith(MockitoExtension.class)
// public class ApplicationsServiceTest {
//   @Mock
//   private ApplicationsRepository applicationsRepository;
//   @Mock
//   private UsersAppDataService usersAppDataService;
//   @InjectMocks
//   private ApplicationsService applicationsService;

//   private final String AUTH_MASTER = "master-key";

//   @BeforeEach
//   void setUp() {
//     ReflectionTestUtils.setField(applicationsService, "auth_master", AUTH_MASTER);
//   }

//   @Test
//   void deveBuscarAplciacaoPeloNome() {
//     String appName = "test-app";
//     Applications app = new Applications();
//     app.setName(appName);
//     when(applicationsRepository.findByName(appName)).thenReturn(Optional.of(app));

//     Applications result = applicationsService.findByName(appName);

//     assertNotNull(result);
//     assertEquals(appName, result.getName());
//   }

//   @Test
//   void registerWithSuccess() {
//     // Ambiente
//     String authorization = AUTH_MASTER;
//     ApplicationDto applicationDto = new ApplicationDto();
//     applicationDto.setName("newapp");
//     applicationDto.setApplicationOfModerador("OldApp");
//     applicationDto.setEmailModerador("moderator@test.com");

//     Users moderator = new Users();
//     moderator.setEmail("moderator@test.com");

//     UsersAppData moderatorOldAppData = new UsersAppData();
//     moderatorOldAppData.setUsers(moderator);
//     moderatorOldAppData.setPassword("encoded-password");

//     Roles roleUser = new Roles();
//     roleUser.setName(ERoles.ROLE_USER);
//     Set<Roles> roles = new HashSet<>();
//     roles.add(roleUser);

//     Roles roleModerator = new Roles();
//     roleModerator.setName(ERoles.ROLE_MODERATOR);

//     when(applicationsRepository.findByName(anyString())).thenReturn(Optional.empty());
//     when(usersAppDataService.findByUsersEmail(applicationDto.getEmailModerador(),
//         applicationDto.getApplicationOfModerador()))
//         .thenReturn(Optional.of(moderatorOldAppData));

//     when(applicationsRepository.save(any(Applications.class))).thenAnswer(invocation -> invocation.getArgument(0));
//     when(usersAppDataService.assignRole(ERoles.ROLE_USER)).thenReturn(roles);
//     when(usersAppDataService.findByRole(ERoles.ROLE_MODERATOR)).thenReturn(roleModerator);

//     // Mock para a parte do Admin opcional
//     when(usersAppDataService.findByUsersEmail("admin@ajudaqui.com", "authentication_ms")).thenReturn(Optional.empty());

//     // Execução
//     Applications result = applicationsService.regsiter(authorization, applicationDto);

//     // Verificação
//     assertNotNull(result);
//     verify(usersAppDataService, times(1)).findByUsersEmail("admin@ajudaqui.com", "authentication_ms");
//     verify(usersAppDataService, times(2)).findByUsersEmail(anyString(), anyString());
//     assertEquals("newapp", result.getName()); // O serviço converte para lowercase
//   }

//   @Test
//   void test_002_registerWithSuccess() {
//     ApplicationDto applicationDto = new ApplicationDto();
//     applicationDto.setName("newapp");
//     applicationDto.setApplicationOfModerador("OldApp");
//     applicationDto.setEmailModerador("moderator@test.com");

//     Users moderator = new Users();
//     moderator.setEmail("moderator@test.com");

//     UsersAppData moderatorOldAppData = new UsersAppData();
//     moderatorOldAppData.setUsers(moderator);
//     moderatorOldAppData.setPassword("encoded-password");

//     Roles roleUser = new Roles();
//     roleUser.setName(ERoles.ROLE_USER);
//     Set<Roles> roles = new HashSet<>();
//     roles.add(roleUser);

//     Roles roleModerator = new Roles();
//     roleModerator.setName(ERoles.ROLE_MODERATOR);

//     when(applicationsRepository.findByName(anyString())).thenReturn(Optional.empty());
//     when(usersAppDataService.findByUsersEmail(applicationDto.getEmailModerador(),
//         applicationDto.getApplicationOfModerador()))
//         .thenReturn(Optional.of(moderatorOldAppData));

//     when(applicationsRepository.save(any(Applications.class))).thenAnswer(invocation -> invocation.getArgument(0));
//     when(usersAppDataService.assignRole(ERoles.ROLE_USER)).thenReturn(roles);
//     when(usersAppDataService.findByRole(ERoles.ROLE_MODERATOR)).thenReturn(roleModerator);
//     when(usersAppDataService.findByUsersEmail("admin@ajudaqui.com", "authentication_ms")).thenReturn(Optional.empty());

//     Applications result = applicationsService.regsiter(AUTH_MASTER, applicationDto);

//     assertNotNull(result);
//     assertEquals("newapp", result.getName());
//     verify(applicationsRepository, atLeastOnce()).save(any(Applications.class));
//     verify(usersAppDataService, atLeastOnce()).save(any(UsersAppData.class));
//   }

//   @Test
//   void test_003_registerShouldThrowExceptionWhenNameIsEmpty() {
//     ApplicationDto dto = new ApplicationDto();
//     dto.setName("");

//     MessageException exception = assertThrows(MessageException.class, () -> {
//       applicationsService.regsiter(AUTH_MASTER, dto);
//     });

//     assertEquals("O campo name não pode estar vazio.", exception.getMessage());
//   }

//   @Test
//   void test_004_registerShouldThrowExceptionWhenApplicationOfModeradorIsEmpty() {
//     ApplicationDto dto = new ApplicationDto();
//     dto.setName("newapp");
//     dto.setApplicationOfModerador("");

//     MessageException exception = assertThrows(MessageException.class, () -> {
//       applicationsService.regsiter(AUTH_MASTER, dto);
//     });

//     assertEquals("O campo aplicação do moderador não pode estar vazio.", exception.getMessage());
//   }

//   @Test
//   void test_005_registerShouldThrowExceptionWhenEmailModeradorIsEmpty() {
//     ApplicationDto dto = new ApplicationDto();
//     dto.setName("newapp");
//     dto.setApplicationOfModerador("oldapp");
//     dto.setEmailModerador("");

//     MessageException exception = assertThrows(MessageException.class, () -> {
//       applicationsService.regsiter(AUTH_MASTER, dto);
//     });

//     assertEquals("O campo email do emailModerador não pode estar vazio.", exception.getMessage());
//   }
// }
