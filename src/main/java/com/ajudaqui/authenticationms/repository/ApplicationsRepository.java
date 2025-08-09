package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Applications;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

    Optional<Applications> findByName(String name);

    Optional<Applications> findByClientId(String clientId);

}
