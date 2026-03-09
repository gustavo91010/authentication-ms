package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.stereotype.Service;

@Service
public class UsersService {

  private UsersRepository userRepository;
  private JwtUtils jwtUtils;
  final private ApplicationsService applicationsService;
  final private UsersAppDataService appDataService;

  public UsersService(UsersRepository userRepository, JwtUtils jwtUtils,
      UsersAppDataService appDataService, ApplicationsService applicationsService) {
    this.userRepository = userRepository;
    this.jwtUtils = jwtUtils;
    this.appDataService = appDataService;
    this.applicationsService = applicationsService;
  }

  public UsersAppData create(UsersRegister usersRegister, boolean isInternal) {
    Applications application = applicationsService.findByName(usersRegister.getAplication());

    String urlRegister = application.getRegisterUrl();
    if (urlRegister == null || urlRegister.isBlank())
      throw new BadRequestException("A Aplicação não tem URL de registro cadastrada");

    appDataService.findByUsersEmail(usersRegister.getEmail(), usersRegister.getAplication())
        .filter(app -> usersRegister.getAplication().equals(app.getApplications().getName()))
        .ifPresent(app -> {
          throw new MessageException("Email já registrado");
        });

    Users users = userRepository.findByEmail(usersRegister.getEmail())
        .orElseGet(() -> save(usersRegister.toUsers(isInternal)));

    UsersAppData usersAppData = usersRegister.toAppData(users, isInternal, application,
        appDataService.assignRole(ERoles.ROLE_USER));
    usersAppData.setOtherFields(usersRegister.getOtherFields());

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

  public Users findByJwt(String jwtToken, String secretKey) {
    return findByEmail(jwtUtils.getEmailFromJwtToken(jwtToken));
  }

  public Users update(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    return save(users);
  }
}
