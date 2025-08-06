package com.ajudaqui.authenticationms.service;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.UsersAppDataRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersAppDataService {

  @Autowired
  private UsersAppDataRepository repository;

  public UsersAppData findByUsersId(Long usersId) {
    return this.repository.findByUsersId(usersId)
        .orElseThrow(() -> new NotFoundException("Usuário não tem dados registardos"));

  }

  public UsersAppData getUsersByEmail(String email) {
    return this.repository.findByUserEmail(email).
    orElseThrow(()-> new NotFoundException("Usuário nao registrado"));
  }
  public Optional<UsersAppData> findByUsersEmail(String email) {
    return this.repository.findByUserEmail(email);
  }

  public UsersAppData save(UsersAppData usersAppData) {
    usersAppData.setProfileData("{\"\": \"\"}");

    return repository.save(usersAppData);
  }

  public UsersAppData findByAccessToken(String accessToken) {
    return repository.findByAccessToken(accessToken)
        .orElseThrow(() -> new NotFoundException("Usuário não tem dados registardos"));

  }
}
