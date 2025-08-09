package com.ajudaqui.authenticationms.config.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ajudaqui.authenticationms.entity.Applications;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.service.ApplicationsService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Autowired
  private ApplicationsService apppaApplicationsService;
  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  private Map<String, String> secretKeys = new HashMap<>();

  public String generatedJwtToken(UsersAppData usersApp) {
    LocalDateTime issuedAt = LocalDateTime.now(ZoneId.systemDefault());
    Date issuedAtDate = Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant());
    LocalDateTime expirationDateTime = issuedAt.plus(jwtExpirationMs, ChronoUnit.MILLIS);
    Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
    // return null;
    return Jwts.builder().setSubject(usersApp.getUsers().getEmail()).setIssuedAt(issuedAtDate)
        .setExpiration(expirationDate)
        .claim("client_id", usersApp.getApplications().getClientId())
        .claim("access_token", usersApp.getAccessToken())
        .signWith(SignatureAlgorithm.HS512, usersApp.getApplications().getSecretId()).compact();

  }

  public String getEmailFromJwtToken(String token) {
    token = token.replace("Bearer ", "");
    String jwtSecret = getSecretKeyByJwt(token);
    if (!validateJwtToken(token, jwtSecret))
      throw new RuntimeException("Token inválido");

    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  private String getSecretKeyByJwt(String token) {
    String[] parts = token.split("\\.");
    String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
    JsonObject payload = JsonParser.parseString(payloadJson).getAsJsonObject();
    String clientId = payload.get("client_id").getAsString();
    if (!secretKeys.keySet().contains(clientId)) {
      Applications application = apppaApplicationsService.getByClientId(clientId);
      secretKeys.put(clientId, application.getSecretId());
    }
    return this.secretKeys.get(clientId);
  }

  private String getClaims(String token, String claim) {
    return Jwts.parser().setSigningKey(getSecretKeyByJwt(token)).parseClaimsJws(token).getBody().get(claim,
        String.class);
  }

  public boolean validateJwtToken(String authToken, String jwtSecret) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
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
