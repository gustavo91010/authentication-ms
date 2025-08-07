package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
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
  private RolesRepository rolesRepository;
  private JwtUtils jwtUtils;
  final private ApplicationsService applicationsService;
  final private UsersAppDataService appDataService;

  public UsersService(UsersRepository userRepository, RolesRepository rolesRepository, JwtUtils jwtUtils,
      UsersAppDataService appDataService, ApplicationsService applicationsService) {
    this.userRepository = userRepository;
    this.rolesRepository = rolesRepository;
    this.jwtUtils = jwtUtils;
    this.appDataService = appDataService;
    this.applicationsService = applicationsService;
  }

  public UsersAppData create(UsersRegister usersRegister, boolean isInternal) {
    Applcations application = applicationsService.findByName(usersRegister.getAplication());
    // Users users = save(usersRegister.toUsers());
    Users users = userRepository.findByEmail(usersRegister.getEmail())
        .orElseGet(() -> save(usersRegister.toUsers(isInternal)));
    UsersAppData usersAppData = appDataService.findByUsersEmail(usersRegister.getEmail())
        .orElseGet(() -> usersRegister.toAppData(users, isInternal, application, assignRole()));
    if (usersAppData.getApplications().getName() == null || usersAppData.getApplications().getName() == null) {
      throw new MessageException("sem o aplicatuion name nm da né...");
    }
    return appDataService.save(usersAppData);
  }

  private Users save(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    users.setCreatedAt(LocalDateTime.now());

    return userRepository.save(users);
  }

  public Users findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  }

  public Users findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  }

  public List<Users> findAll() {
    return userRepository.findAll();
  }

  public Set<Roles> assignRole() {
    Set<Roles> roles = new HashSet<>();
    Roles user_role = rolesRepository.findByName(ERoles.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
    roles.add(user_role);
    return roles;
  }

  public Users findByJwt(String jwtToken, String secretKey) {
    return findByEmail(jwtUtils.getEmailFromJwtToken(jwtToken));
  }

  public Users update(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    return save(users);
  }
}
