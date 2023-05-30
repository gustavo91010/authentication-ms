package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.authenticationms.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByUsername(String username);

	Optional<Users> findByEmail(String email);

	@Query(value = "SELECT COUNT(*) > 0 FROM users WHERE username= :username ", nativeQuery = true)
	Boolean existsByUsername(String username);

	@Query(value = "SELECT COUNT(*) > 0 FROM users WHERE email= :email ", nativeQuery = true)
	Boolean existsByEmail(String email);

}
