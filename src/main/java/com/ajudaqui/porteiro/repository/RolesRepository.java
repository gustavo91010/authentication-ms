package com.ajudaqui.porteiro.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ajudaqui.porteiro.entity.Roles;
import com.ajudaqui.porteiro.utils.enuns.ERoles;

public interface RolesRepository extends MongoRepository<Roles, Long>{
	
	Optional<Roles> findByName(ERoles name);

}
