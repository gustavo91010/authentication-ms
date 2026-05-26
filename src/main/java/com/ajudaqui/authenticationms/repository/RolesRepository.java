package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

public interface RolesRepository extends MongoRepository<Roles, Long>{
	
	Optional<Roles> findByName(ERoles name);

}
