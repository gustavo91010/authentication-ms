package com.ajudaqui.authenticationms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.exception.MesageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Service
public class UsersService {

	@Autowired
	private UsersRepository userRepository;
	@Autowired
	private RolesRepository rolesRepository;

	public Users create(UsersRegister usersRegister) {
		Users users = usersRegister.toDate();
		users.setRoles(assignRole());

		userRepository.save(users);
		return users;

	}

	public Users findByUsername(String username) {
		Optional<Users> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new MesageException("Usuario não encontrado");
		}
		return user.get();

	}

	public Users findByEmail(String email) {
		Optional<Users> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new MesageException("Usuario não encontrado");
		}
		return user.get();

	}
	public Users findById(Long id) {
		Optional<Users> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new MesageException("Usuario não encontrado");
		}
		return user.get();
	}
	public List<Users> findAll() {
		return userRepository.findAll();
	}

	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	// Definir ROLE
	public Set<Roles> assignRole() {

		Set<Roles> roles = new HashSet<>();
		Roles user_role = rolesRepository.findByName(ERoles.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
		roles.add(user_role);

		return roles;
	}

}
