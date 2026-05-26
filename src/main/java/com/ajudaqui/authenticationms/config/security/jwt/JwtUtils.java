package com.ajudaqui.authenticationms.config.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.ajudaqui.authenticationms.entity.*;
import com.ajudaqui.authenticationms.service.ApplicationsService;
import com.ajudaqui.authenticationms.utils.enuns.ERoles;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Autowired
  private ApplicationsService apppaApplicationsService;
  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  private Map<String, String> secretKeys = new HashMap<>();

  private Key getSigningKey(String secret) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generatedJwtToken(UsersAppData usersApp) {
    // UsersAppData usersApp= users
    LocalDateTime issuedAt = LocalDateTime.now(ZoneId.systemDefault());
    Date issuedAtDate = Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant());
    LocalDateTime expirationDateTime = issuedAt.plus(jwtExpirationMs, ChronoUnit.MILLIS);
    Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
    List<ERoles> roles = usersApp.getRoles().stream()
        .map(Roles::getName)
        .collect(Collectors.toList());

    String appName = usersApp.getAppName();
    if (!secretKeys.containsKey(appName)) {
      Applications application = apppaApplicationsService.findByName(appName);
      secretKeys.put(appName, application.getSecretId());
    }

    String secretKey = secretKeys.get(appName);
    return Jwts.builder()
        // .setSubject(usersApp.getUsers().getEmail())
        .setIssuedAt(issuedAtDate)
        .setExpiration(expirationDate)
        .claim("roles", roles)
        .claim("application", usersApp.getAppName())
        .claim("access_token", usersApp.getAccessToken())
        .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS512)
        .compact();
  }

  public String getAppFromJwtToken(String token) {
    token = token.replace("Bearer ", "");
    String jwtSecret = getSecretKeyByJwt(token);
    if (!validateJwtToken(token, jwtSecret))
      throw new RuntimeException("Token inválido");

    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey(jwtSecret))
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("application").toString();
  }

  public String getEmailFromJwtToken(String token) {
    token = token.replace("Bearer ", "");
    String jwtSecret = getSecretKeyByJwt(token);
    if (!validateJwtToken(token, jwtSecret))
      throw new RuntimeException("Token inválido");

    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey(jwtSecret))
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  private String getSecretKeyByJwt(String token) {
    String[] parts = token.split("\\.");
    if (parts.length < 2)
      throw new RuntimeException("Token inválido");

    String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
    JsonObject payload = JsonParser.parseString(payloadJson).getAsJsonObject();
    String clientId = payload.get("client_id").getAsString();
    if (!secretKeys.containsKey(clientId)) {
      Applications application = apppaApplicationsService.getByClientId(clientId);
      secretKeys.put(clientId, application.getSecretId());
    }
    return this.secretKeys.get(clientId);
  }

  public boolean validateJwtToken(String authToken, String jwtSecret) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(getSigningKey(jwtSecret))
          .build()
          .parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
