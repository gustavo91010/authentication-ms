package com.ajudaqui.authenticationms.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ajudaqui.authenticationms.config.security.jwt.JwtUtils;
import com.ajudaqui.authenticationms.config.security.service.UserDetailsImpl;
import com.ajudaqui.authenticationms.controller.CustomerRegister;
import com.ajudaqui.authenticationms.entity.Client;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.LoginRequest;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.model.ClientRegister;

@Service
public class AuthService {
	Logger logger= LoggerFactory.getLogger(AuthService.class);
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ClientService clientService;

	@Autowired
	private JwtUtils jwtUtils;

	public void registerConsumer() {
		
	}

	public LoginResponse authenticateUser(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generatedJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		LoginResponse loginResponse = new LoginResponse();

		loginResponse.setId(userDetails.getId());
		loginResponse.setUsername(userDetails.getUsername());
		loginResponse.setJwt(jwt);
		loginResponse.setRoles(roles);

		return loginResponse;

	}

	public Client registerClient(ClientRegister clientRegister) {
		logger.info("Solicitação de registro de Client");
		// Verificando se usuario já possue registro
		if (clientService.existsByUsername(clientRegister.getUsername())) {
			logger.error("Usuário já cadastrado.");
			throw new MessageException("Usuário já cadastrado.");
		}

		return clientService.create(clientRegister);

	}

	public void registerCustomer(@Valid CustomerRegister consumerRegister) {
		logger.info("Solicitação de registro de Client");
//		// Verificando se usuario já possue registro
//		if (clientService.existsByUsername(clientRegister.getUsername())) {
//			logger.error("Usuário já cadastrado.");
//			throw new MessageException("Usuário já cadastrado.");
//		}
//
//		return clientService.create(clientRegister);
		
	}

}
