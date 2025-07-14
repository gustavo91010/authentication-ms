package com.ajudaqui.authenticationms.controller;
import com.ajudaqui.authenticationms.service.LoginGithubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login/github")
public class LoginGithubController {
// verficar se a rota esta liberada para ser chamada

  @Autowired
  private LoginGithubService loginGithubService;

  @GetMapping
  public ResponseEntity<Void> redirecinarGithub() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(loginGithubService.factorUri());

    return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
  }

}
