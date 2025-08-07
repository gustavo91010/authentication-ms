package com.ajudaqui.authenticationms.config.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.ajudaqui.authenticationms.dto.UsersAppApplicationDto;
import com.ajudaqui.authenticationms.entity.Users;
import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.exception.MessageException;
import com.ajudaqui.authenticationms.service.ApplicationsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Autowired
  private ApplicationsService apppaApplicationsService;
  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generatedJwtToken(UsersAppData usersApp) {
    LocalDateTime issuedAt = LocalDateTime.now(ZoneId.systemDefault());
    Date issuedAtDate = Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant());
    LocalDateTime expirationDateTime = issuedAt.plus(jwtExpirationMs, ChronoUnit.MILLIS);
    Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());
    System.out.println("clientId: " + usersApp.getApplications().getClientId());
    System.out.println("secretId usada na assinatura: " + usersApp.getApplications().getSecretId());
    // return null;
    return Jwts.builder().setSubject(usersApp.getUsers().getEmail()).setIssuedAt(issuedAtDate)
        .setExpiration(expirationDate)
        .claim("clientId", usersApp.getApplications().getClientId())
        .claim("access_token", usersApp.getAccessToken())
        .signWith(SignatureAlgorithm.HS512, usersApp.getApplications().getSecretId()).compact();

  }

  public String getEmailFromJwtToken(String token) {
    token = token.replace("Bearer ", "");
    String clientId = getClaims(token, "clientId");
    String jwtSecret = apppaApplicationsService.getByClientId(clientId).getSecretId();
    System.out.println("secret " + jwtSecret);

    if (!validateJwtToken(token, jwtSecret))
      throw new RuntimeException("Token inválido");

    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  private String getClaims(String token, String claim) {

    token = token.replace("Bearer ", "");
    return Jwts.parser().parseClaimsJws(token).getBody().get(claim, String.class);
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
