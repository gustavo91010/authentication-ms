package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.dto.ApplicationDto;
import com.ajudaqui.authenticationms.dto.HttpUsersAppData;
import com.ajudaqui.authenticationms.entity.Applcations;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.ApplicationsRepository;

import org.springframework.stereotype.Service;

@Service
public class ApplicationsService {
  final private ApplicationsRepository repository;
  final private UsersAppDataService usersAppDataService;

  public ApplicationsService(ApplicationsRepository applicationsRepository, UsersAppDataService usersAppDataService) {
    this.repository = applicationsRepository;
    this.usersAppDataService = usersAppDataService;
  }

  public Applcations findByName(String name) {
    return repository.findByName(name)
        .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  }

  public Applcations regsiter(ApplicationDto appicationDto) {
    String name = appicationDto.getName().toLowerCase();
    if (repository.findByName(name).isPresent())
      throw new MessageException("Nome já registrado");
    return update(appicationDto.toEntity());
  }

  public List<HttpUsersAppData> userByApp(String name) {
    Applcations byName = findByName(name);
    List<UsersAppData> byAppId = usersAppDataService.findByAppId(byName.getId());
    return byAppId.stream().map(HttpUsersAppData::new)
        .collect(Collectors.toList());
  }

  public Applcations getOrRegister(String name) {
    return repository.findByName(name)
        .orElseGet(() -> update(new Applcations(name, "")));
  }

  public Applcations update(Applcations applcations) {
    applcations.setUpdatedAt(LocalDateTime.now());
    return this.repository.save(applcations);
  }

  public Applcations getByClientId(String clientId) {
    return repository.findByClientId(clientId)
        .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  }

}
