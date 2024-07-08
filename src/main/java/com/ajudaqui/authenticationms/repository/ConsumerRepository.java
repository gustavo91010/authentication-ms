package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajudaqui.authenticationms.entity.Consumer;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

	Optional<Consumer> findByLogin(String login);


}
