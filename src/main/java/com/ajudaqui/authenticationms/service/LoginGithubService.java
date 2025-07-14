package com.ajudaqui.authenticationms.service;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import software.amazon.awssdk.services.sqs.endpoints.internal.Value.Str;

@Service
public class LoginGithubService {

  @Value("${oauth.github.client-id}")
  private String clientId = "";

  @Value("${oauth.github.client-secret}")
  private String clientSecret = "";

  @Value("${oauth.github.redirect-uri}")
  private String redirectUri = "";

  // @Autowired
  private final RestTemplate restTemplate;

  public LoginGithubService(RestTemplateBuilder build) {
    this.restTemplate = build.build();
  }

  public URI factorUri() {
    String urlGithub = "https://github.com/login/oauth/authorize";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=read:user,user:email"; // Quais informações do usuário pegar...
    System.out.println("params " + params);
    return URI.create(urlGithub + params + scope);
  }

  public String obterToken(String authorization_code) {
    String url = "https://github.com/login/oauth/access_token";

    Map<String, String> body = new HashMap<>();
    body.put("code", authorization_code);
    body.put("client_id", clientId);
    body.put("client_secret", clientSecret);
    body.put("client_secret", clientSecret);

    Arrays.asList("");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON); // Enviar o conteudo tipo json
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); // espero receber tipo json

    HttpEntity<Map<String, String>> request = new HttpEntity<>(body, httpHeaders);

    return restTemplate.postForEntity(url, request, String.class).getBody();
  }

}
