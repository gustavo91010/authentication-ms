package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.Applcations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationsRepository extends JpaRepository<Applcations, Long> {

    Optional<Applcations> findByName(String name);

}
