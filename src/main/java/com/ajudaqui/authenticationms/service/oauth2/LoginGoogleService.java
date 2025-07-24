package com.ajudaqui.authenticationms.service.oauth2;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ajudaqui.authenticationms.request.DataUserOAuth2;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.AuthService;

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
public class LoginGoogleService {

  @Autowired
  private AuthService authService;
  @Value("${oauth.google.client-id}")
  private String clientId;

  @Value("${oauth.google.client-secret}")
  private String clientSecret;

  @Value("${oauth.google.redirect-uri}")
  private String redirectUri;

  private final RestTemplate restTemplate;

  public LoginGoogleService(RestTemplateBuilder build) {
    this.restTemplate = build.build();
  }

  public URI authorizedUri() {
    String urlApis = "https://www.googleapis.com"; // la n google cloud
    String urlScope = "/auth/userinfo.email";
    String urlGithub = "https://accounts.google.com/o/oauth2/v2/auth";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=" + urlApis + urlScope;
    String responseType = "&response_type=code";

    return URI.create(urlGithub + params + scope + responseType);
  }

  public URI registerUri() {
    String urlGithub = "https://github.com/login/oauth/authorize";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=read:user,user:email"; // Quais informações do usuário pegar...
    return URI.create(urlGithub + params + scope);
  }

  private String getToken(String authorization_code) {
    String url = "https://oauth2.googleapis.com/token";
    Map<String, String> body = new HashMap<>();
    body.put("code", authorization_code);
    body.put("client_id", clientId);
    body.put("client_secret", clientSecret);
    body.put("redirect_uri", redirectUri);
    body.put("grant_type", "authorization_code");

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON); // Enviar o conteudo tipo json
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); // espero receber tipo json

    HttpEntity<Map<String, String>> request = new HttpEntity<>(body, httpHeaders);
    return restTemplate.postForEntity(url, request, Map.class).getBody().get("access_token").toString();
  }

  public LoginResponse authorized(String code) {
    return authService.authenticateUser(getUserData(code).getEmail());
  }

  private DataUserOAuth2 getUserData(String authorization_code) {
    String token = getToken(authorization_code);

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    HttpEntity<Object> entity = new HttpEntity<>(headers);

    ResponseEntity<DataUserOAuth2> userResponse = restTemplate.exchange(
        "https://www.googleapis.com/oauth2/v2/userinfo",
        HttpMethod.GET,
        entity,
        DataUserOAuth2.class);
    return userResponse.getBody();
  }
}
