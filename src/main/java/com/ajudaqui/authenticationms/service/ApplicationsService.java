package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Applcations;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.ApplicationsRepository;

import org.springframework.stereotype.Service;

@Service
public class ApplicationsService {
  final private ApplicationsRepository repository;

  public ApplicationsService(ApplicationsRepository applicationsRepository) {
    this.repository = applicationsRepository;
  }

  public Applcations findByName(String name) {
    return repository.findByName(name)
        .orElseThrow(() -> new NotFoundException("Aplicação não registrada"));
  }

  public Applcations getOrRegister(String name) {
    return repository.findByName(name)
        .orElseGet(() -> update(new Applcations(name)));
  }

  public Applcations update(Applcations applcations) {
    applcations.setUpdatedAt(LocalDateTime.now());
    return this.repository.save(applcations);
  }

}
