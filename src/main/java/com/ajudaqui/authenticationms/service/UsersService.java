package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.stereotype.Service;

@Service
public class UsersService {

  private UsersRepository userRepository;
  final private ApplicationsService applicationsService;
  private RolesRepository rolesRepository;

  public UsersService(UsersRepository userRepository,
      ApplicationsService applicationsService, RolesRepository rolesRepository) {
    this.userRepository = userRepository;
    this.applicationsService = applicationsService;
    this.rolesRepository = rolesRepository;
  }

  public UsersAppData create(UsersRegister usersRegister, boolean isInternal) {
    Applications application = applicationsService.findByName(usersRegister.getApplication());
    String urlRegister = application.getRegisterUrl();
    if (urlRegister == null || urlRegister.isBlank())
      throw new BadRequestException("A Aplicação não tem URL de registro cadastrada");

    boolean emailRegistradoNaAplicacao = userRepository
        .findByEmailAndUsersAppDataAppName(usersRegister.getEmail(), usersRegister.getApplication())
        .isPresent();

    if (emailRegistradoNaAplicacao)
      throw new MessageException("Email já registrado");

    Users users = usersRegister.toAppData(usersRegister, isInternal, assignRole(ERoles.ROLE_USER));

    save(users);
    return users.getUsersAppData().stream()
        .filter(app -> usersRegister.getApplication().equals(app.getAppName()))
        .findFirst()
        .get();
  }

  private Users save(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    users.setCreatedAt(LocalDateTime.now());

    return userRepository.save(users);
  }

  public UsersAppData findByAccessToken(UUID accessToken) {
    return userRepository.findByUsersAppDataAccessToken(accessToken)
        .map(user -> user.getUsersAppData().stream()
            .filter(app -> accessToken.equals(app.getAccessToken()))
            .findFirst()
            .orElseThrow(() -> new MessageException("Usuario não encontrado")))
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));

  }

  public Users findByEmail(String email, String appName) {
    Users user = userRepository.findByEmailAndUsersAppDataAppName(email, appName)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));

    user.setUsersAppData(
        user.getUsersAppData()
            .stream()
            .filter(app -> appName.equals(app.getAppName()))
            .collect(Collectors.toSet()));
    return user;
  }

  public List<Users> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public Users findById(String id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  }

  public List<Users> findAll() {
    return userRepository.findAll();
  }

  public Users update(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    return save(users);
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

  public UsersAppData getUsersByEmail(String email, String application) {
    return this.findByEmail(email, application)
        .getUsersAppData()
        .iterator().next();
  }

  public Map<String, String> getData(String accessToken) {
    Users userApp = userRepository.findByUsersAppDataAccessToken(UUID.fromString(accessToken))
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));

    Map<String, String> data = new HashMap<>();
    data.put("access_token", accessToken);
    String name = userApp.getEmail();
    if (userApp.getName() != null)
      name = userApp.getName();
    data.put("name", name);

    data.put("email", userApp.getEmail());
    data.put("aplication", userApp.getUsersAppData().iterator().next().getAppName());
    return data;
  }
}
