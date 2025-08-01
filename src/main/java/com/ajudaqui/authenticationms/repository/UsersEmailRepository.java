package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.UsersEmail;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersEmailRepository extends JpaRepository<UsersEmail, Long> {

    Optional<UsersEmail> findByEmail(String email);

}
