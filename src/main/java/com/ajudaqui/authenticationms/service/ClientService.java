package com.ajudaqui.authenticationms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Client;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.service.model.UsersRegister;
import com.ajudaqui.authenticationms.repository.ClientRepository;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private RolesRepository rolesRepository;

	public Client create(UsersRegister usersRegister) {
		Client users = usersRegister.toDate();
		users.setRoles(assignRole());

		clientRepository.save(users);
		return users;

	}

	public Client findByUsername(String email) {
		Optional<Client> user = clientRepository.findByEmail(email);
		if (!user.isPresent()) {
			throw new MessageException("Usuario não encontrado");
		}
		return user.get();

	}

	public Client findById(Long id) {
		Optional<Client> user = clientRepository.findById(id);
		if (user.isPresent()) {
			throw new MessageException("Usuario não encontrado");
		}
		return user.get();
	}
	public List<Client> findAll() {
		return clientRepository.findAll();
	}

	public Boolean existsByUsername(String username) {
		return clientRepository.existsByEmail(username);
	}


	// Definir ROLE
	public Set<Roles> assignRole() {

		Set<Roles> roles = new HashSet<>();
		Roles user_role = rolesRepository.findByName(ERoles.ROLE_CLIENT)
				.orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
		roles.add(user_role);

		return roles;
	}

}
