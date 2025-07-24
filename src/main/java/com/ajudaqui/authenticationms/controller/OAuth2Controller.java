package com.ajudaqui.authenticationms.controller;

import com.ajudaqui.authenticationms.service.oauth2.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ui.Model;
// import springfox.documentation.schema.Model;

@RestController
@RequestMapping("/login")
public class OAuth2Controller {

  @Autowired
  private GithubService githubService;

  @Autowired
  private GoogleService googleService;

  @GetMapping("/google")
  public ResponseEntity<Void> redirecinarGoogle() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(googleService.authorizedUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
  }

  @GetMapping("/google/user")
  public ResponseEntity<Void> registerGoogle() {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(googleService.registerUri());
    return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
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
  public ResponseEntity<?> authorized(@RequestParam String code) {
    return ResponseEntity.ok(githubService.authorized(code));
  }

  // @GetMapping("/register/auth2")
  // public ResponseEntity<?> showRegisterForm(@RequestParam String email, @RequestParam String name, Model model) {
  //   model.addAttribute("email", email);
  //   model.addAttribute("name", name);
  //   // return "register";
  //   HttpHeaders headers = new HttpHeaders();
  //   return new ResponseEntity<>(headers, HttpStatus.FOUND); // tem que devolver um code 302
  // }

  // @PostMapping("/register/google")
  // public ResponseEntity<String> processRegisterGoogleForm(
  //     @RequestParam String email,
  //     @RequestParam String name,
  //     @RequestParam String password,
  //     @RequestParam String confirmPassword) {

  //   if (!password.equals(confirmPassword)) {
  //     return ResponseEntity.badRequest().body("Senhas não conferem.");
  //   }

  //   // Aqui, salva o usuário no banco, criptografa a senha etc.

  //   return ResponseEntity.ok("Usuário registrado com sucesso!");
  // }

}
