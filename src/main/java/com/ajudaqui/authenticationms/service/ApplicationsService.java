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
  final private UsersAppDataService usersAppDataService;

  @Value("${app.auth.master}")
  private String auth_master;

  public ApplicationsService(ApplicationsRepository applicationsRepository, UsersAppDataService usersAppDataService) {
    this.repository = applicationsRepository;
    this.usersAppDataService = usersAppDataService;
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

    String name = appicationDto.getName().toLowerCase();

    if (repository.findByName(name).isPresent())
      throw new MessageException("Nome já registrado");

    UsersAppData moderatorOldApp = usersAppDataService
        .findByUsersEmail(appicationDto.getEmailModerador(), appicationDto.getApplicationOfModerador())
        .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));

    Applications newApp = save(appicationDto.toEntity());

    Set<Roles> roles = usersAppDataService.assignRole(ERoles.ROLE_USER);
    roles.add(usersAppDataService.findByRole(ERoles.ROLE_MODERATOR));

    UsersAppData moderatorNewApp = new UsersAppData();
    moderatorNewApp.setUsers(moderatorOldApp.getUsers());
    moderatorNewApp.setApplications(newApp);
    moderatorNewApp.setPassword(moderatorOldApp.getPassword());
    moderatorNewApp.setActive(true);
    moderatorNewApp.setRoles(roles);
    moderatorNewApp.setAccessToken(UUID.randomUUID());
    moderatorNewApp.setCreatedAt(LocalDateTime.now());
    moderatorNewApp.setUpdatedAt(LocalDateTime.now());
    usersAppDataService.save(moderatorNewApp);

    return newApp;
  }

  public List<HttpUsersAppData> userByApp(String email, String appName) {
    Applications byName = findByName(appName);
    checkPermission(email, appName, ERoles.ROLE_MODERATOR, ERoles.ROLE_ADMIN);

    List<UsersAppData> byAppId = usersAppDataService.findByAppId(byName.getId());
    return byAppId.stream().map(HttpUsersAppData::new)
        .collect(Collectors.toList());
  }

  private void checkPermission(String email, String application, ERoles... allowedRoles) {
    UsersAppData user = usersAppDataService.getUsersByEmail(email, application);
    boolean hasPermission = user.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(r -> {
          for (ERoles allowed : allowedRoles) {
            if (allowed.equals(r)) return true;
          }
          return false;
        });

    if (!hasPermission)
      throw new MessageException("Solicitação não autorizada");
  }

  public UsersAppData assignAdmin(String moderatorEmail, String appName, String userEmail) {
    Applications app = findByName(appName);
    checkPermission(moderatorEmail, appName, ERoles.ROLE_MODERATOR);

    UsersAppData userAppData = usersAppDataService.getUsersByEmail(userEmail, appName);

    boolean alreadyAdmin = userAppData.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(ERoles.ROLE_ADMIN::equals);

    if (alreadyAdmin)
      throw new MessageException("Usuário já é ADMIN nesta aplicação");

    Roles adminRole = usersAppDataService.findByRole(ERoles.ROLE_ADMIN);
    userAppData.getRoles().add(adminRole);
    return usersAppDataService.save(userAppData);
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
