package com.ajudaqui.authenticationms.service;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.DataUserOAuth2;
import com.ajudaqui.authenticationms.request.dto.DataEmailGithub;
import com.ajudaqui.authenticationms.response.LoginResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginGithubService {

  @Autowired
  private AuthService authService;
  @Value("${oauth.github.client-id}")
  private String clientId;

  @Value("${oauth.github.client-secret}")
  private String clientSecret;

  @Value("${oauth.github.redirect-uri}")
  private String redirectUri;

  // @Autowired
  private final RestTemplate restTemplate;

  public LoginGithubService(RestTemplateBuilder build) {
    this.restTemplate = build.build();
  }

  public URI authorizedUri() {
    String urlGithub = "https://github.com/login/oauth/authorize";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=read:user,user:email"; // Quais informações do usuário
    // pegar...
    System.out.println("params " + params);
    return URI.create(urlGithub + params + scope);
  }

  public URI registerUri() {
    String urlGithub = "https://github.com/login/oauth/authorize";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=read:user,user:email"; // Quais informações do usuário pegar...
    System.out.println("params " + params);
    return URI.create(urlGithub + params + scope);
  }

  private String getToken(String authorization_code) {
    String url = "https://github.com/login/oauth/access_token";

    Map<String, String> body = new HashMap<>();
    body.put("code", authorization_code);
    body.put("client_id", clientId);
    body.put("client_secret", clientSecret);
    body.put("client_secret", clientSecret);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON); // Enviar o conteudo tipo json
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); // espero receber tipo json

    HttpEntity<Map<String, String>> request = new HttpEntity<>(body, httpHeaders);
    return restTemplate.postForEntity(url, request, Map.class).getBody().get("access_token").toString();
  }

  private String obterEmail(String authorization_code) {
    String url = "https://api.github.com/user/emails";
    String token = getToken(authorization_code);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(token);
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    return getEmail(new HttpEntity<>(httpHeaders));
  }

  public LoginResponse authorized(String code) {
    Map<String, String> obterDadosUsuario = getUserData(code);
    // return authService.authenticateUser(obterEmail(code));
    return authService.authenticateUser(obterDadosUsuario.get("email"));
  }

  private Map<String, String> getUserData(String authorization_code) {
    String token = getToken(authorization_code);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<Object> entity = new HttpEntity<>(headers);

    ResponseEntity<DataUserOAuth2> userResponse = restTemplate.exchange(
        "https://api.github.com/user",
        HttpMethod.GET,
        entity,
        DataUserOAuth2.class);

    DataUserOAuth2 user = userResponse.getBody();

    String email = user.getEmail();

    if (email == null || email.isEmpty()) {
      email = getEmail(entity);
    }
    Map<String, String> result = new HashMap<>();
    result.put("name", user.getName());
    result.put("email", email);

    return result;
  }

  private String getEmail(HttpEntity<Object> entity) {
    ResponseEntity<DataEmailGithub[]> emailsResponse = restTemplate.exchange(
        "https://api.github.com/user/emails",
        HttpMethod.GET,
        entity,
        DataEmailGithub[].class);

    return Arrays.stream(emailsResponse.getBody())
        .filter(DataEmailGithub::getPrimary)
        .filter(DataEmailGithub::getVerified)
        .findFirst()
        .orElseThrow(() -> new MessageException("Email não localizado"))
        .getEmail();
  }
}
