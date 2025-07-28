package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.config.security.totp.TotpService;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {

  @Autowired
  private UsersRepository userRepository;
  @Autowired
  private RolesRepository rolesRepository;
  @Autowired
  private TotpService totpService;
  @Autowired
  private JwtUtils jwtUtils;

  public Users create(UsersRegister usersRegister) {
    Users users = usersRegister.toDate();
    users.setRoles(assignRole());
    users.setAplication(usersRegister.getAplication());
    return save(users);
  }

  private Users save(Users users) {
    users.setUpdate_at(LocalDateTime.now());
    users.setCreated_at(LocalDateTime.now());
    return userRepository.save(users);
  }

  public Users findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  }

  public Users findByAccessToken(String accessToken) {
    return userRepository.findByAccessToken(accessToken)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  }

  public Users findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new MessageException("Usuario não encontrado"));
  }

  public List<Users> findAll() {
    return userRepository.findAll();
  }

  public Boolean existsByEmail(String username) {
    return userRepository.findByEmail(username).isPresent();
  }

  public Set<Roles> assignRole() {
    Set<Roles> roles = new HashSet<>();
    Roles user_role = rolesRepository.findByName(ERoles.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
    roles.add(user_role);
    return roles;
  }

  public Users findByJwt(String jwtToken) {
    return findByEmail(jwtUtils.getEmailFromJwtToken(jwtToken));
  }

  @Transactional
  public String generatedQrCode(String jwtToken) {
    Users users = findByJwt(jwtToken);
    users.setSecret(totpService.generatedSecret());
    update(users);
    return totpService.generatedQrCode(users.getEmail(), users.getSecret());
  }

  private Users update(Users users) {
    users.setUpdate_at(LocalDateTime.now());
    return save(users);
  }
}
