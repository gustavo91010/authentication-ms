package com.ajudaqui.authenticationms.service;

import java.util.*;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersAppDataRepository;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersAppDataService {

  @Autowired
  private UsersService usersService;

  @Autowired
  private RolesRepository rolesRepository;

  public UsersAppData findByAccessToken(String accessToken) {
    return this.usersService.findByAccessToken(accessToken)
        .orElseThrow(() -> new NotFoundException("Usuário não tem dados registardos"));
  }

  public UsersAppData getUsersByEmail(String email, String application) {
    return this.usersService.findByEmail(email, application);
  }

  public Optional<UsersAppData> findByUsersEmail(String email, String application) {
    List<UsersAppData> byUserEmail = this.usersService.findByUserEmail(email);
    if (byUserEmail.isEmpty())
      return Optional.empty();
    return byUserEmail.stream()
        .filter(u -> application.equals(u.getApplications().getName()))
        .findFirst();
  }

  public UsersAppData save(UsersAppData usersAppData) {
    return usersService.save(usersAppData);
  }

  public Map<String, String> getData(String accessToken) {
    UsersAppData userApp = findByAccessToken(UUID.fromString(accessToken));

    Map<String, String> data = new HashMap<>();
    data.put("access_token", userApp.getAccessToken().toString());
    String name = userApp.getUsers().getEmail();
    if (userApp.getUsers().getName() != null)
      name = userApp.getUsers().getName();
    data.put("name", name);

    data.put("email", userApp.getUsers().getEmail());
    data.put("aplication", userApp.getApplications().getName());
    return data;
  }

  public UsersAppData findByAccessToken(UUID accessToken) {
    return usersService.findByAccessToken(accessToken)
        .orElseThrow(() -> new NotFoundException("Usuário não tem dados registardos"));

  }

  public Roles findByRole(ERoles role) {
    return rolesRepository.findByName(role)
        .orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
  }

  public Set<Roles> assignRole(ERoles role) {
    Set<Roles> roles = new HashSet<>();
    roles.add(findByRole(role));
    return roles;
  }

}
