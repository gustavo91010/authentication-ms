package com.ajudaqui.authenticationms.controller;

import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.request.UsersRegister;
import com.ajudaqui.authenticationms.service.AuthService;
import com.ajudaqui.authenticationms.service.oauth2.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/login")
public class SocialAuthController {

  @Autowired
  private GithubService githubService;
  @Autowired
  private AuthService authService;

  @Autowired
  private GoogleService googleService;

  @GetMapping("/google")
  public ResponseEntity<Void> redirecinarGoogle() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(googleService.authorizedUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @GetMapping("/google/user")
  public ResponseEntity<Void> registerGoogle() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(googleService.registerUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND);
  }

  @GetMapping("/google/autorizado")
  public ResponseEntity<?> authorizedGoohle(@RequestParam String code) {

    // eu chamo o de cima.. com a uri que constri, e ele chama esse aqui, me passand
    // os dads de auth do cliente
    return ResponseEntity.ok(googleService.authorized(code));
  }

  @GetMapping("/github")
  public ResponseEntity<Void> redirecinarGithub() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(githubService.authorizedUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
  }

  @GetMapping("/github/autorizado")
  public String authorized(@RequestParam String code, Model model) {
    return githubService.authorized(code, model);
  }

  @PostMapping("/register-auth2")
  public String registerByGithub(
      @RequestParam String email,
      @RequestParam String name,
      @RequestParam String password,
      @RequestParam String confirmPassword,
      @RequestParam String appName,
      @RequestParam String urlLogin) {

    UsersRegister usersRegister = new UsersRegister();
    usersRegister.setName(name);
    usersRegister.setEmail(email);

    usersRegister.setPassword(password);
    usersRegister.setAplication(appName);
    return urlLogin + authService.registerUser(usersRegister).getAccess_token();
  }
}
