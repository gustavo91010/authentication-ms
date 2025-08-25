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
  private UsersAppDataRepository repository;

  @Autowired
  private RolesRepository rolesRepository;

  public UsersAppData findByUsersId(Long usersId) {
    return this.repository.findByUsersId(usersId)
        .orElseThrow(() -> new NotFoundException("Usuário não tem dados registardos"));
  }

  public List<UsersAppData> findByAppId(Long appId) {
    return repository.findByAppId(appId);
  }

  public UsersAppData getUsersByEmail(String email) {
    return this.repository.findByUserEmail(email).orElseThrow(() -> new NotFoundException("Usuário nao registrado"));
  }

  public Optional<UsersAppData> findByUsersEmail(String email) {
    return this.repository.findByUserEmail(email);
  }

  public UsersAppData save(UsersAppData usersAppData) {

    return repository.save(usersAppData);
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
    return repository.findByAccessToken(accessToken)
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
