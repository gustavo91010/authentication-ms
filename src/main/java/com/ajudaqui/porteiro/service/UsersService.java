package com.ajudaqui.porteiro.service;

import java.time.LocalDateTime;
import java.util.*;
import com.ajudaqui.porteiro.entity.*;
import com.ajudaqui.porteiro.exception.BadRequestException;
import com.ajudaqui.porteiro.exception.MessageException;
import com.ajudaqui.porteiro.exception.NotFoundException;
import com.ajudaqui.porteiro.repository.ApplicationsRepository;
import com.ajudaqui.porteiro.repository.RolesRepository;
import com.ajudaqui.porteiro.repository.UsersRepository;
import com.ajudaqui.porteiro.request.UsersRegister;
import com.ajudaqui.porteiro.utils.enuns.ERoles;

import org.springframework.stereotype.Service;

@Service
public class UsersService {

  private UsersRepository userRepository;
  final private ApplicationsRepository applicationsService;
  private RolesRepository rolesRepository;

  public UsersService(UsersRepository userRepository,
      ApplicationsRepository applicationsService, RolesRepository rolesRepository) {
    this.userRepository = userRepository;
    this.applicationsService = applicationsService;
    this.rolesRepository = rolesRepository;
  }

  public Users create(UsersRegister usersRegister) {

    Applications application = applicationsService.findById(usersRegister.getAppId())
        .orElseThrow(() -> new NotFoundException("Aplicação nao registrada"));

    String urlRegister = application.getRegisterUrl();
    if (urlRegister == null || urlRegister.isBlank())
      throw new BadRequestException("A Aplicação não tem URL de registro cadastrada");

    boolean emailRegistradoNaAplicacao = userRepository
        .findByEmailAndUsersAppDataAppId(usersRegister.getEmail(), usersRegister.getAppId())
        .isPresent();

    if (emailRegistradoNaAplicacao)
      throw new MessageException("Email já registrado");

    Users users = usersRegister.toAppData(assignRole(ERoles.ROLE_USER));
    UsersAppData appData = users.selectApp(usersRegister.getAppId());

    String urlLoginByToken = application.getLoginByTokenUrl();
    boolean isloginByToken = !(urlLoginByToken == null || urlLoginByToken.isBlank());
    if (isloginByToken)
      appData.setActive(!isloginByToken);

    save(users);

    if (appData == null)
      throw new MessageException("Porblema no registro do usuário");

    return users;
  }

  private Users save(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    users.setCreatedAt(LocalDateTime.now());

    return userRepository.save(users);
  }

  public Users findByAccessToken(UUID accessToken) {
    return userRepository.findByUsersAppDataAccessToken(accessToken)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));

    // UsersAppData appData = user.selectApp(accessToken);
    // if (appData == null)
    // throw new MessageException("Usuario não encontrado");

    // return appData;

  }

  public UsersAppData getUsersByEmail(String email, String appId) {
    Users user = userRepository.findByEmailAndUsersAppDataAppId(email, appId)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));

    UsersAppData appData = user.selectApp(appId);
    if (appData == null)
      throw new MessageException("Usuario não encontrado");

    return appData;
  }

  public Optional<Users> findByEmail(String email, String appId) {
    return userRepository.findByEmailAndUsersAppDataAppId(email, appId);

  }

  public Users findByEmail(String email) {
    System.out.println("-------------------------------");
    System.out.println("email "+email);
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new MessageException(String.format("Email %s não registrado", email)));
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
    return userRepository.save(users);
  }

  public Roles findByRole(ERoles role) {
    return rolesRepository.findByName(role)
        .orElseThrow(() -> new MessageException("Roles não encontrado."));
  }

  public Set<Roles> assignRole(ERoles role) {
    Set<Roles> roles = new HashSet<>();
    roles.add(findByRole(role));
    return roles;
  }

  public Map<String, String> getData(String accessToken) {
    Users userApp = userRepository.findByUsersAppDataAccessToken(UUID.fromString(accessToken))
        .orElseThrow(() -> new MessageException(String.format("Usuario do %s não encontrado", accessToken.toString())));

    Map<String, String> data = new HashMap<>();
    data.put("access_token", accessToken);
    String name = userApp.getEmail();
    if (userApp.getName() != null)
      name = userApp.getName();
    data.put("name", name);

    data.put("email", userApp.getEmail());
    data.put("aplication", userApp.getUsersAppData().iterator().next().getAppId());
    return data;
  }

  public List<UsersAppData> findByAppId(String appId) {
    var appData = userRepository.findByUsersAppDataAppId(appId);
    if (appData.isEmpty())
      throw new NotFoundException(String.format("Aplicação %s não registrada", appId));

    return appData.stream()
        .map(data -> data.selectApp(appId))
        .toList();
  }

}
