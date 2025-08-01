
package com.ajudaqui.authenticationms.service;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.UsersEmail;
import com.ajudaqui.authenticationms.repository.UsersEmailRepository;

import org.springframework.stereotype.Service;

@Service
public class UsersEmailService {

  final private UsersEmailRepository repository;

  public UsersEmailService(UsersEmailRepository repository) {
    this.repository = repository;
  }

  public Optional<UsersEmail> findByEmail(String email) {
    return repository.findByEmail(email);
  }

}
