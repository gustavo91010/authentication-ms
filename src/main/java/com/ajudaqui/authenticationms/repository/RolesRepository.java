package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajudaqui.authenticationms.models.Roles;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

public interface RolesRepository extends JpaRepository<Roles, Long>{
	
	Optional<Roles> findByName(ERoles name);

}
