package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.authenticationms.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

  // Optional<Users> findByEmail(String email);

  // @Query(value = "SELECT * FROM users WHERE access_token= :accessToken", nativeQuery = true)
  // Optional<Users> findByAccessToken(String accessToken);

  // @Query(value = "SELECT COUNT(*) > 0 FROM users WHERE email= :email ",
  // nativeQuery = true)
  // Boolean existsByEmail(String email);

}
