package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajudaqui.authenticationms.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Optional<Client> findByEmail(String username);

	@Query(value = "SELECT COUNT(*) > 0 FROM users WHERE email= :email ", nativeQuery = true)
	Boolean existsByEmail(String email);

}
