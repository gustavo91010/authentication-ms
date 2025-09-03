package com.ajudaqui.authenticationms.service.oauth2;

import static java.util.Arrays.asList;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.request.DataUserOAuth2;
import com.ajudaqui.authenticationms.request.dto.DataEmailGithub;
import com.ajudaqui.authenticationms.response.LoginResponse;
import com.ajudaqui.authenticationms.service.AuthService;
import com.ajudaqui.authenticationms.service.PageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubService {

  @Autowired
  private AuthService authService;
  @Value("${oauth.github.client-id}")
  private String clientId;

  @Value("${oauth.github.client-secret}")
  private String clientSecret;

  @Value("${oauth.github.redirect-uri}")
  private String redirectUri;
  @Autowired
  private PageService pageService;
  private final RestTemplate restTemplate;

  public GithubService(RestTemplateBuilder build) {
    this.restTemplate = build.build();
  }

  public URI authorizedUri() {
    String urlGithub = "https://github.com/login/oauth/authorize";
    String params = "?client_id=" + clientId + "&redirect_uri=" + redirectUri;
    String scope = "&scope=read:user,user:email"; // Quais informações do usuário
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
    httpHeaders.setContentType(APPLICATION_JSON);
    httpHeaders.setAccept(asList(APPLICATION_JSON));

    HttpEntity<Map<String, String>> request = new HttpEntity<>(body, httpHeaders);
    return restTemplate.postForEntity(url, request, Map.class).getBody().get("access_token").toString();
  }

  public String authorized(String code, Model model) {
    Map<String, String> data = getUserData(code);
    String urlRegister = "http://localhost:8082/login/register-auth2";
    String urlLogin = "redirect:http://localhost:3000/?token=";
    String application = "bill-manager";
    try {
      LoginResponse login = authService.authenticateUser(data.get("email"), application);
      return urlLogin + login.getAccess_token();
    } catch (Exception e) {
      return pageService.showRegisterForm(application, urlLogin, urlRegister, data.get("email"), data.get("name"),
          model);
    }
  }

  private Map<String, String> getUserData(String authorization_code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(getToken(authorization_code));
    headers.setAccept(asList(APPLICATION_JSON));

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
