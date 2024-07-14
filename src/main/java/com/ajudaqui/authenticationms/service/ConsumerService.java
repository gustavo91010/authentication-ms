package com.ajudaqui.authenticationms.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.entity.Consumer;
import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.repository.ConsumerRepository;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.model.ConsumerRegister;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

@Service
public class ConsumerService {
	Logger logger = LoggerFactory.getLogger(ConsumerService.class);

	@Autowired
	private ConsumerRepository consumerRepositpry;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private AuthService authService;

	public Consumer create(ConsumerRegister consumerRegister) {
		logger.info("Criação de um Consumer");
		Consumer consumer = consumerRegister.toDate();
		consumer.setRoles(assignRole());
		consumerRepositpry.save(consumer);

		return consumer;
	}

	public String authenticatin(String login, String password) {
		Optional<Consumer> consumer = consumerRepositpry.findByLogin(login);
		if (!consumer.isPresent()) {

			logger.error("usuário / senha incorreto");
			throw new MessageException("usuário / senha incorreto");
		}
		if (new BCryptPasswordEncoder().matches(password, consumer.get().getPassword())) {

			logger.error("usuário / senha incorreto");
			throw new MessageException("usuário / senha incorreto");
		}

		logger.info("Solicitação de autenticação efetuada com sucesso");
		LoginResponse auth = authService.authenticateUser(new LoginRequest(login, password));

		return auth.getJwt();
	}

	// Definir ROLE
	public Set<Roles> assignRole() {

		Set<Roles> roles = new HashSet<>();
		Roles user_role = rolesRepository.findByName(ERoles.ROLE_CONSUMER)
				.orElseThrow(() -> new RuntimeException("Erro: Type Roles não encontrado."));
		roles.add(user_role);

		return roles;
	}

}
