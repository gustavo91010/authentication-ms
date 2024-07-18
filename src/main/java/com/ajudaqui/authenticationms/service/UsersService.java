package com.ajudaqui.authenticationms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.exception.MesageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Service
public class UsersService {
	Logger logger = LoggerFactory.getLogger(UsersService.class);

	@Autowired
	private UsersRepository userRepository;
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private JwtUtils jwtUtils;

	public Users create(UsersRegister usersRegister) {
		Users users = usersRegister.toDate();
		users.setRoles(assignRole());
		users.setAplication(usersRegister.getAplication());
		
		
		System.out.println("users " + users.toString());
		userRepository.save(users);
		return users;

	}

	public Users findByEmail(String email) {
		Optional<Users> user = userRepository.findByEmail(email);
		if (!user.isPresent()) {
			throw new MesageException("Usuario não encontrado");
		}
		return user.get();

	}

	public Users findById(Long id) {
		Optional<Users> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new MesageException("Usuario não encontrado");
		}
		return user.get();
	}

	public List<Users> findAll() {
		return userRepository.findAll();
	}

	public Boolean existsByEmail(String username) {
		return userRepository.findByEmail(username).isPresent();
	}

	// Definir ROLE
	public Set<Roles> assignRole() {

		Set<Roles> roles = new HashSet<>();
		Roles user_role = rolesRepository.findByName(ERoles.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
		roles.add(user_role);

		return roles;
	}

	public Users findByJwt(String jwtToken) {
		String email= jwtUtils.getEmailFromJwtToken(jwtToken);

		return findByEmail(email);
	}

}
