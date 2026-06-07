package com.ajudaqui.porteiro.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ajudaqui.porteiro.entity.Users;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {

  // List<Users> findByEmail(String email);
  Optional<Users> findByEmail(String email);

  Optional<Users> findByEmailAndUsersAppDataAppId(
      String email,
      String appId);

  // busca os appData pelo nome da palicação
  List<Users> findByUsersAppDataAppId(String appId);

  Optional<Users> findByUsersAppDataAccessToken(UUID accessToken);
}
