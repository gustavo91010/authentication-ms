package com.ajudaqui.authenticationms.controller;

import com.ajudaqui.authenticationms.service.LoginGoogleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/google")
public class LoginGoogleController {
  // verficar se a rota esta liberada para ser chamada

  @Autowired
  private LoginGoogleService loginGoogleService;

  @GetMapping()
  public ResponseEntity<Void> redirecinarGithub() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(loginGoogleService.authorizedUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
  }
  @GetMapping("/user")
  public ResponseEntity<Void> register() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(loginGoogleService.registerUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
  }

  @GetMapping("/autorizado")
  public ResponseEntity<?> authorized(@RequestParam String code) {

    // eu chamo o de cima.. com a uri que constri, e ele chama esse aqui, me passand
    // os dads de auth do cliente
    return ResponseEntity.ok(loginGoogleService.authorized(code));
  }
}
