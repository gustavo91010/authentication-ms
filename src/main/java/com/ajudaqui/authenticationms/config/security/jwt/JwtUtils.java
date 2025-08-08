package com.ajudaqui.authenticationms.config.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.ajudaqui.authenticationms.entity.UsersAppData;
import com.ajudaqui.authenticationms.service.ApplicationsService;

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
    System.out.println("2");
    System.out.println("token " + token);
parece qu etem que ter a key d etodo jetio...
    token = token.replace("Bearer ", "");
    System.out.println("3");
    String clientId = getClaims(token, "client_id");
    String jwtSecret = apppaApplicationsService.getByClientId(clientId).getSecretId();

    if (!validateJwtToken(token, jwtSecret))
      throw new RuntimeException("Token inválido");

    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  private String getClaims(String token, String secret, String claim) {
    System.out.println("4");
    if (token == null)
      throw new RuntimeException("Token inválido");

    System.out.println("5 token " + token);
    Claims shua = Jwts.parser().parseClaimsJws(token).getBody();

    System.out.println("claisn " + claim);
    System.out.println("sua " + shua);
    shua.get(claim, String.class);
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get(claim, String.class);
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
