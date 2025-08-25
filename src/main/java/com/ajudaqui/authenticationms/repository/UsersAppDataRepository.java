package com.ajudaqui.authenticationms.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ajudaqui.authenticationms.entity.UsersAppData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersAppDataRepository extends JpaRepository<UsersAppData, Long> {

  @Query(value = "SELECT uap.* FROM users u LEFT JOIN user_app_data uap ON u.id = uap.user_id WHERE u.email = :email", nativeQuery = true)
  Optional<UsersAppData> findByUserEmail(@Param("email") String email);

  @Query(value = "SELECT * FROM user_app_data WHERE user_id=:usersId", nativeQuery = true)
  Optional<UsersAppData> findByUsersId(Long usersId);

  @Query(value = "SELECT * FROM user_app_data WHERE access_token=:accessToken", nativeQuery = true)
  Optional<UsersAppData> findByAccessToken(UUID accessToken);

  @Query(value = "SELECT * FROM user_app_data WHERE app_id= :appId", nativeQuery = true)
  List<UsersAppData> findByAppId(Long appId);

  // Optional<UsersAppData> findByUsersId(Long usersId);

}
