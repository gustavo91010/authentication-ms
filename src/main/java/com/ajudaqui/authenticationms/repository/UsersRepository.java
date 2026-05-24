package com.ajudaqui.authenticationms.repository;

import java.util.List;
import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Users;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {

  List<Users> findByEmail(String email);

  Optional<Users> findByEmailAndUsersAppDataAppName(
      String email,
      String appName);

  // busca os appData pelo nome da palicação
  List<Users> findByUsersAppDataAppName(String appName);
}
