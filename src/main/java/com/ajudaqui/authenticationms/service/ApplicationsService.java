package com.ajudaqui.authenticationms.service;

import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.*;
import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.*;
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
      throw new MessageException("Já existe uma aplicação registrada com esse nome.");

    Users moderator = usersService
        .findByEmail(appicationDto.getEmailModerador(), appicationDto.getApplicationOfModerador())
        .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));

    UsersAppData appDataModerador = moderator.selectApp(appicationDto.getApplicationOfModerador());

    Applications newApp = save(appicationDto.toEntity());

    Set<Roles> roles = usersService.assignRole(ERoles.ROLE_USER);
    roles.add(usersService.findByRole(ERoles.ROLE_MODERATOR));

    moderator.getUsersAppData().add(
        new UsersAppData().newApp(newApp.getName(),
            appDataModerador.getPassword(),
            true,
            roles));
    // Atualizando o moderador na nova aplicação
    usersService.update(moderator);

    // --- NOVO: Garante registro do Admin padrão (admin@ajudaqui.com)
    try {
      Users authUser = usersService.findByEmail("admin@ajudaqui.com", "authentication_ms")
          .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));

      UsersAppData appDataAuth = authUser.selectApp("authentication_ms");
      usersService.findByRole(ERoles.ROLE_ADMIN);

      roles.add(usersService.findByRole(ERoles.ROLE_ADMIN));
      authUser.getUsersAppData().add(
          new UsersAppData().newApp(newApp.getName(),
              appDataAuth.getPassword(),
              true,
              roles));

      // Registrando o ADMIN na nova aplicação
      usersService.update(moderator);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return newApp;
  }

  public List<HttpUsersAppData> userByApp(String email, String appName) {
    checkPermission(email, appName, ERoles.ROLE_MODERATOR, ERoles.ROLE_ADMIN);

    List<UsersAppData> byAppId = usersService.findByAppName(appName);
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

    Users user = usersService.findByEmail(userEmail);
    UsersAppData userAppData = user.selectApp(appName);

    boolean alreadyAdmin = userAppData.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(ERoles.ROLE_ADMIN::equals);

    if (alreadyAdmin)
      throw new MessageException("Usuário já é ADMIN nesta aplicação");

    Roles adminRole = usersService.findByRole(ERoles.ROLE_ADMIN);
    userAppData.getRoles().add(adminRole);
    usersService.update(user);
    return userAppData;
  }

  public Applications getOrRegister(String name) {
    return repository.findByName(name)
        .orElseGet(() -> save(new Applications(name, "")));
  }

  // TODO isso ta só a carcaça
  public Applications update(String email, String appName, ApplicationDto dto) {
    return save(dto.toUpdate(findByName(appName)));
  }

  // public Applications findById(String email, Long applicationId) {
  // return repository.findById(applicationId)
  // .map(a -> {
  // checkPermission(email, a.getName(), ERoles.ROLE_MODERATOR);
  // return a;
  // })
  // .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  // }

  private Applications save(Applications applications) {
    return repository.save(applications);
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
