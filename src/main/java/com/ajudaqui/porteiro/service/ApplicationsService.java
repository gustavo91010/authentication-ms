package com.ajudaqui.porteiro.service;

import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.porteiro.dto.*;
import com.ajudaqui.porteiro.entity.*;
import com.ajudaqui.porteiro.exception.*;
import com.ajudaqui.porteiro.repository.ApplicationsRepository;
import com.ajudaqui.porteiro.utils.enuns.ERoles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsService {
  final private ApplicationsRepository repository;
  final private UsersService usersService;

  @Value("${app.auth.master}")
  private String auth_master;

  public ApplicationsService(ApplicationsRepository applicationsRepository, UsersService usersService) {
    this.repository = applicationsRepository;
    this.usersService = usersService;
  }

  public Applications findByName(String name) {
    return repository.findByName(name)
        .orElseThrow(() -> new NotFoundException("Aplicação " + name + " não registrada."));
  }

  public Applications register(String authorization, ApplicationDto applicationDto) {

    checkingPermission(authorization);

    if (applicationDto.getName() == null || applicationDto.getName().isEmpty())
      throw new MessageException("O campo name não pode estar vazio.");

    if (applicationDto.getAppIdOfModerador() == null || applicationDto.getAppIdOfModerador().isEmpty())
      throw new MessageException("O campo appId do moderador não pode estar vazio.");

    if (applicationDto.getEmailModerador() == null || applicationDto.getEmailModerador().isEmpty())
      throw new MessageException("O campo email do emailModerador não pode estar vazio.");

    if (applicationDto.getSecret() != null && applicationDto.getSecret().length() < 88) {
      throw new IllegalArgumentException("Secret inválida");
    }
    
    String name = applicationDto.getName().toLowerCase();
    applicationDto.setName(name); // Normalize name to lowercase

    if (repository.findByName(name).isPresent())
      throw new MessageException("Já existe uma aplicação registrada com esse nome.");

    Users moderator = usersService
        .findByEmail(applicationDto.getEmailModerador(), applicationDto.getAppIdOfModerador())
        .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));

    UsersAppData appDataModerador = moderator.selectApp(applicationDto.getAppIdOfModerador());

    Applications newApp = save(applicationDto.toEntity());

    Set<Roles> roles = usersService.assignRole(ERoles.ROLE_USER);
    roles.add(usersService.findByRole(ERoles.ROLE_MODERATOR));

    moderator.getUsersAppData().add(
        new UsersAppData().newApp(newApp.getId(), newApp.getName(),
            appDataModerador.getPassword(),
            true,
            roles));
    // Atualizando o moderador na nova aplicação
    usersService.update(moderator);

    // --- NOVO: Garante registro do Admin padrão (admin@ajudaqui.com)
    try {
      String PorteiroAppId = "6a1ba9823a434a40267d4042";
      Optional<Users> porteiroUserOpt = usersService.findByEmail("admin@ajudaqui.com", PorteiroAppId);
      
      if (porteiroUserOpt.isPresent()) {
          Users porteiroUser = porteiroUserOpt.get();
          // Criando app data do Admin
          UsersAppData newAdminData = new UsersAppData();
          newAdminData.setAppName(newApp.getName());
          newAdminData.setAppId(newApp.getId());
          newAdminData.setPassword(porteiroUser.selectApp(PorteiroAppId).getPassword());
          newAdminData.setActive(true);
          newAdminData.setAccessToken(UUID.randomUUID());

          Set<Roles> adminRoles = new HashSet<>(roles);
          adminRoles.add(usersService.findByRole(ERoles.ROLE_ADMIN));
          newAdminData.setRoles(adminRoles);

          porteiroUser.getUsersAppData().add(newAdminData);
          usersService.update(porteiroUser);
      } else {
          System.err.println("Admin padrão não encontrado para registro na nova aplicação.");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return newApp;
  }

  public List<HttpUsersAppData> userByApp(String email, String appId) {
    checkPermission(email, appId, ERoles.ROLE_MODERATOR, ERoles.ROLE_ADMIN);

    List<UsersAppData> byAppId = usersService.findByAppId(appId);
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

  public UsersAppData assignAdmin(String moderatorEmail, String appId, String userEmail) {
    checkPermission(moderatorEmail, appId, ERoles.ROLE_MODERATOR);

    Users user = usersService.findByEmail(userEmail);
    UsersAppData userAppData = user.selectApp(appId);

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

  public Applications update(String email, String appId, ApplicationDto dto) {
    return save(dto.toUpdate(findByName(appId)));
  }

  public Applications findById(String applicationId) {
    return repository.findById(applicationId)
        .orElseThrow(() -> new NotFoundException("Aplicação de ID " + applicationId + " não está registrada."));
  }

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
