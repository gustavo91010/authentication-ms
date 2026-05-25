package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.ApplicationDto;
import com.ajudaqui.authenticationms.dto.HttpAplications;
import com.ajudaqui.authenticationms.dto.HttpUsersAppData;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.ApplicationsRepository;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsService {
  final private ApplicationsRepository repository;
  final private UsersService usersService;

  @Value("${app.auth.master}")
  private String auth_master;

  public ApplicationsService(ApplicationsRepository applicationsRepository, UsersService usersAppDataService) {
    this.repository = applicationsRepository;
    this.usersService = usersAppDataService;
  }

  public Applications findByName(String name) {
    return repository.findByName(name)
        .orElseThrow(() -> new NotFoundException("Aplicação não " + name + " registrada."));
  }

  public Applications regsiter(String authorization, ApplicationDto appicationDto) {

    checkingPermission(authorization);

    if (appicationDto.getName() == null || appicationDto.getName().isEmpty())
      throw new MessageException("O campo name não pode estar vazio.");

    if (appicationDto.getApplicationOfModerador() == null || appicationDto.getApplicationOfModerador().isEmpty())
      throw new MessageException("O campo aplicação do moderador não pode estar vazio.");

    if (appicationDto.getEmailModerador() == null || appicationDto.getEmailModerador().isEmpty())
      throw new MessageException("O campo email do emailModerador não pode estar vazio.");

    if (appicationDto.getSecret() != null && appicationDto.getSecret().length() < 88) {
      throw new IllegalArgumentException("Secret inválida");
    }
    String name = appicationDto.getName().toLowerCase();

    if (repository.findByName(name).isPresent())
      throw new MessageException("Nome já registrado");

    Users moderatorOldApp = usersService
        .findByEmail(appicationDto.getEmailModerador(), appicationDto.getApplicationOfModerador())
        .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));

    UsersAppData appDataModerador = moderatorOldApp.getUsersAppData().stream()
        .filter(app -> appicationDto.getApplicationOfModerador().equals(app.getAppName()))
        .findFirst()
        .get();

    Applications newApp = save(appicationDto.toEntity());

    Set<Roles> roles = usersService.assignRole(ERoles.ROLE_USER);
    roles.add(usersService.findByRole(ERoles.ROLE_MODERATOR));

    moderatorOldApp.getUsersAppData().add(
        new UsersAppData().newApp(newApp.getName(),
            appDataModerador.getPassword(),
            true,
            roles));
    // Registrando o moderador na nova aplicação
    usersService.update(moderatorOldApp);

    // --- NOVO: Garante registro do Admin padrão (admin@ajudaqui.com)
    try {
      Users authUser = usersService.findByEmail("admin@ajudaqui.com", "authentication_ms")
          .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));

      UsersAppData appDataAuth = authUser.getUsersAppData().stream()
          .filter(app -> "authentication_ms".equals(app.getAppName()))
          .findFirst()
          .get();
      //
      usersService.findByRole(ERoles.ROLE_ADMIN);

      roles.add(usersService.findByRole(ERoles.ROLE_ADMIN));
      authUser.getUsersAppData().add(
          new UsersAppData().newApp(newApp.getName(),
              appDataAuth.getPassword(),
              true,
              roles));

      // Registrando o ADMIN na nova aplicação
      usersService.update(moderatorOldApp);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return newApp;
  }

  public List<HttpUsersAppData> userByApp(String email, String appName) {
    Applications byName = findByName(appName);
    checkPermission(email, appName, ERoles.ROLE_MODERATOR, ERoles.ROLE_ADMIN);

    List<UsersAppData> byAppId = usersService.findByAppId(byName.getId());
    return byAppId.stream().map(HttpUsersAppData::new)
        .collect(Collectors.toList());
  }

  private void checkPermission(String email, String application, ERoles... allowedRoles) {
    UsersAppData user = usersService.getUsersByEmail(email, application);
    boolean hasPermission = user.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(r -> {
          for (ERoles allowed : allowedRoles) {
            if (allowed.equals(r))
              return true;
          }
          return false;
        });

    if (!hasPermission)
      throw new MessageException("Solicitação não autorizada");
  }

  public UsersAppData assignAdmin(String moderatorEmail, String appName, String userEmail) {
    checkPermission(moderatorEmail, appName, ERoles.ROLE_MODERATOR);

    UsersAppData userAppData = usersService.getUsersByEmail(userEmail, appName);

    boolean alreadyAdmin = userAppData.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(ERoles.ROLE_ADMIN::equals);

    if (alreadyAdmin)
      throw new MessageException("Usuário já é ADMIN nesta aplicação");

    Roles adminRole = usersService.findByRole(ERoles.ROLE_ADMIN);
    userAppData.getRoles().add(adminRole);
    return usersService.save(userAppData);
  }

  public Applications getOrRegister(String name) {
    return repository.findByName(name)
        .orElseGet(() -> save(new Applications(name, "")));
  }

  public Applications update(String email, Long applicationId, ApplicationDto dto) {
    return save(dto.toUpdate(findById(email, applicationId)));
  }

  public Applications findById(String email, Long applicationId) {
    return repository.findById(applicationId)
        .map(a -> {
          checkPermission(email, a.getName(), ERoles.ROLE_MODERATOR);
          return a;
        })
        .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  }

  private Applications save(Applications applications) {
    return repository.save(applications);
  }

  public Applications getByClientId(String clientId) {
    return repository.findByClientId(clientId)
        .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  }

  public List<HttpAplications> findAll(String authorization) {
    checkingPermission(authorization);
    return repository.findAll().stream()
        .map(HttpAplications::new)
        .collect(Collectors.toList());
  }

  private void checkingPermission(String authorization) {
    if (!auth_master.equals(authorization))
      throw new BadRequestException("Solicitação não autorizada!");
  }
}
