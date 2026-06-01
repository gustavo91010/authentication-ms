package com.ajudaqui.authenticationms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.repository.ApplicationsRepository;
import com.ajudaqui.authenticationms.repository.RolesRepository;
import com.ajudaqui.authenticationms.repository.UsersRepository;
import com.ajudaqui.authenticationms.request.UsersRegister;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

  @Mock
  private UsersRepository userRepository;
  @Mock
  private ApplicationsRepository applicationsService;
  @Mock
  private RolesRepository rolesRepository;
  @InjectMocks
  private UsersService usersService;

  @Test
  void deveCriarUserData() {
    // Ambiente
    UsersRegister usersRegister = new UsersRegister();

    // verificação:
    Users users = usersService.create(usersRegister);

    assertEquals("lalala", users.getName());
  }

  void deveIncluirUserData() {

  }
}
