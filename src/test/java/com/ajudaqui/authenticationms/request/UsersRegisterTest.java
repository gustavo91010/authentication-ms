package com.ajudaqui.authenticationms.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import com.ajudaqui.authenticationms.entity.Roles;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsersRegisterTest {

  private UsersRegister usersRegister;
  private Set<Roles> roles;

  @BeforeEach
  void setUp() {
    usersRegister = new UsersRegister();
    usersRegister.setName("Test User");
    usersRegister.setEmail("test@example.com");
    usersRegister.setAppId("test-app");

    roles = new HashSet<>();
    Roles role = new Roles();
    role.setName(ERoles.ROLE_USER);
    roles.add(role);
  }

  @Test
  void deveLancarExceptionParaSenhaCurta() {
    usersRegister.setPassword("aB@4567"); // 7 caracteres

    MessageException exception = assertThrows(MessageException.class, () -> {
      usersRegister.toAppData(roles);
    });

    assertEquals("A senha deve ter pelo menos 8 caracters", exception.getMessage());
  }

  @Test
  void deveLancarExceptionParaSenhaSemMaiusculaOuMinuscula() {
    usersRegister.setPassword("senha123!"); // Tudo minusculo

    MessageException exception = assertThrows(MessageException.class, () -> {
      usersRegister.toAppData(roles);
    });

    assertEquals("A senha deve ter pelo menos uma letra maiúscula e uma minuscula", exception.getMessage());
  }

  @Test
  void deveLancarExceptionParaSenhaSemCaracterEspecial() {
    usersRegister.setPassword("Senha1234");

    MessageException exception = assertThrows(MessageException.class, () -> {
      usersRegister.toAppData(roles);
    });

    assertEquals("A senha deve ter pelo menos um caracter especial (@,#,$,%,&,*,-,_)", exception.getMessage());
  }

  @Test
  void deveAceitarSenhaForteECriptografar() {
    usersRegister.setPassword("SenhaForte@123");

    var user = usersRegister.toAppData(roles);

    assertNotNull(user);
    String encryptedPassword = user.getUsersAppData().iterator()
        .next().getPassword();

    // Verifica se a senha foi criptografada (BCrypt começa com $2a$ ou $2b$)
    assertTrue(encryptedPassword.startsWith("$2a$") || encryptedPassword.startsWith("$2y$"));
    assertNotEquals("SenhaForte@123", encryptedPassword);
  }
}
