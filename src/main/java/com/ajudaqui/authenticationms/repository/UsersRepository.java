package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

}
