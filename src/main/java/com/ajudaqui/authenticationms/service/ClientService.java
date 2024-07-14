package com.ajudaqui.authenticationms.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.entity.Client;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.service.model.ClientRegister;
import com.ajudaqui.authenticationms.repository.ClientRepository;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Service
public class ClientService {
	Logger logger = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private RolesRepository rolesRepository;

	public Client create(ClientRegister usersRegister) {
		Client client = usersRegister.toDate();
		client.setRoles(assignRole());
		client.setAccess_token(UUID.randomUUID().toString());
		client.setSecret_key(UUID.randomUUID().toString());
		client.setUpdate_at(LocalDateTime.now());


		clientRepository.save(client);
		logger.info("Cliente salvo com sucesso!");
		return client;

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
