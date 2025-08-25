package com.ajudaqui.authenticationms.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.ApplicationDto;
import com.ajudaqui.authenticationms.dto.HttpAplications;
import com.ajudaqui.authenticationms.dto.HttpUsersAppData;
import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.ApplicationsRepository;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.stereotype.Service;

@Service
public class ApplicationsService {
  final private ApplicationsRepository repository;
  final private UsersAppDataService usersAppDataService;

  public ApplicationsService(ApplicationsRepository applicationsRepository, UsersAppDataService usersAppDataService) {
    this.repository = applicationsRepository;
    this.usersAppDataService = usersAppDataService;
  }

  public Applications findByName(String name) {
    return repository.findByName(name)
        .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  }

  public Applications regsiter(ApplicationDto appicationDto) {
    if (appicationDto.getName() == null || appicationDto.getName().isEmpty())
      throw new MessageException("O campo name não pode estar vazio.");
    String name = appicationDto.getName().toLowerCase();
    if (repository.findByName(name).isPresent())
      throw new MessageException("Nome já registrado");

    if (appicationDto.getEmailModerador() == null || appicationDto.getEmailModerador().isEmpty())
      throw new MessageException("O campo email do emailModerador não pode estar vazio.");
    UsersAppData usersAppData = usersAppDataService.findByUsersEmail(appicationDto.getEmailModerador())
        .orElseThrow(() -> new MessageException("O moderador tem que estar registrado previamente"));
    Roles moderator = usersAppDataService.findByRole(ERoles.ROLE_MODERATOR);
    usersAppData.getRoles().add(moderator);
    return save(appicationDto.toEntity());
  }

  public List<HttpUsersAppData> userByApp(String email, String appName) {
    Applications byName = findByName(appName);
    checkPermission(email, byName.getClientId());

    List<UsersAppData> byAppId = usersAppDataService.findByAppId(byName.getId());
    return byAppId.stream().map(HttpUsersAppData::new)
        .collect(Collectors.toList());
  }

  private void checkPermission(String email, String clientId) {
    UsersAppData user = usersAppDataService.getUsersByEmail(email);
    boolean isAdm = user.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(r -> ERoles.ROLE_MODERATOR.equals(r));
    if (isAdm)
      return;
    boolean appPermission = user.getApplications().getClientId().equals(clientId);
    boolean rolesPermission = user.getRoles().stream()
        .map(Roles::getName)
        .anyMatch(r -> ERoles.ROLE_MODERATOR.equals(r));
    if (!appPermission || !rolesPermission)
      throw new MessageException("Solicitação não autorizada");

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
          checkPermission(email, a.getClientId());
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

  public List<HttpAplications> findAll() {
    return repository.findAll().stream()
      .map(HttpAplications::new)
      .collect(Collectors.toList());
  }

}
