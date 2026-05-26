package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Applications;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationsRepository extends MongoRepository<Applications, String> {

  Optional<Applications> findByName(String name);

  Optional<Applications> findBySecretId(String secretId);

}
