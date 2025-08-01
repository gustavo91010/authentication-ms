package com.ajudaqui.authenticationms.service;

import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.UsersAppDataRepository;

import org.springframework.stereotype.Service;

@Service
public class UsersAppDataService {

  final private UsersAppDataRepository repository;

  public UsersAppDataService(UsersAppDataRepository repository) {
    this.repository = repository;
  }

  public UsersAppData findByUsersId(Long usersId) {
    return this.repository.findByUsersId(usersId)
        .orElseThrow(() -> new NotFoundException("Usuário não tem dados registardos"));

  }

}
