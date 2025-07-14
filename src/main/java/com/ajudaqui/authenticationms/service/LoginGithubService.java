package com.ajudaqui.authenticationms.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoginGithubService {

  @Value("${oauth.github.client-id}")
  private String clientId = "";
  @Value("${oauth.github.redirect-uri}")
  private String redirectUri = "";

  public URI factorUri() {
    String urlGithub = "https://github.com/login/oauth/authorize";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=read:user,user:email"; // Quais informações do usuário pegar...
    System.out.println("params " + params);
    return URI.create(urlGithub + params + scope);
  }

}
