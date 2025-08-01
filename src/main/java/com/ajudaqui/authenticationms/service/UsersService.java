package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.config.security.totp.TotpService;
import com.ajudaqui.authenticationms.entity.Applcations;
import com.ajudaqui.authenticationms.entity.Applcations;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.entity.UsersEmail;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {

  private UsersRepository userRepository;
  private RolesRepository rolesRepository;
  private JwtUtils jwtUtils;
  final private ApplicationsService applicationsService;
  final private UsersAppDataService appDataService;
  final private UsersEmailService emailService;

  public UsersService(UsersRepository userRepository, RolesRepository rolesRepository, JwtUtils jwtUtils,
      UsersAppDataService appDataService, UsersEmailService emailService, ApplicationsService applicationsService) {
    this.userRepository = userRepository;
    this.rolesRepository = rolesRepository;
    this.jwtUtils = jwtUtils;
    this.appDataService = appDataService;
    this.emailService = emailService;
    this.applicationsService= applicationsService;
  }

  public Users create(UsersRegister usersRegister) {
    Optional<UsersEmail> users = emailService.findByEmail(usersRegister.getEmail());
Applcations orRegister = applicationsService.getOrRegister(usersRegister.getAplication());
    // .orElse(factoeUserEmail(usersRegister)).getUser();
    if (users.isPresent()) {
      UsersAppData lalal = appDataService.findByUsersId(users.get().getId());
      String password = new BCryptPasswordEncoder().encode(usersRegister.getPassword());
      lalal.setRoles(assignRole());
      lalal.setPassword(password);

      lalal.setAppId(orRegister.getId());
    } else {
      UsersEmail huhui = factoeUserEmail(usersRegister);
    }
    // users.setRoles(assignRole());
    // users.setAplication(usersRegister.getAplication());
    // return save(users);
    return null;
  }

  private UsersEmail factoeUserEmail(UsersRegister usersRegister) {
    return new UsersEmail(save(new Users(usersRegister.getName())), usersRegister.getEmail());
  }

  private Users save(Users users) {
    users.setUpdatedAt(LocalDateTime.now());
    users.setCreatedAt(LocalDateTime.now());
    return userRepository.save(users);
  }

  // public Users findByEmail(String email) {
  // return userRepository.findByEmail(email)
  // .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  // }

  // public Users findByAccessToken(String accessToken) {
  // return userRepository.findByAccessToken(accessToken)
  // .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  // }

  // public Users findById(Long id) {
  // return userRepository.findById(id)
  // .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  // }

  // public List<Users> findAll() {
  // return userRepository.findAll();
  // }

  // public Boolean existsByEmail(String username) {
  // return userRepository.findByEmail(username).isPresent();
  // }

  public Set<Roles> assignRole() {
    Set<Roles> roles = new HashSet<>();
    Roles user_role = rolesRepository.findByName(ERoles.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
    roles.add(user_role);
    return roles;
  }

  // public Users findByJwt(String jwtToken) {
  // return findByEmail(jwtUtils.getEmailFromJwtToken(jwtToken));
  // }

  // @Transactional
  // public String generatedQrCode(String jwtToken) {
  // Users users = findByJwt(jwtToken);
  // users.setSecret(totpService.generatedSecret());
  // update(users);
  // return totpService.generatedQrCode(users.getEmail(), users.getSecret());
  // }

  // public Users update(Users users) {
  // users.setUpdate_at(LocalDateTime.now());
  // return save(users);
  // }
}
