package com.ajudaqui.authenticationms.service;

import static java.time.Instant.now;
import static java.time.LocalDateTime.ofInstant;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.transaction.Transactional;

import com.ajudaqui.authenticationms.entity.Token;
import com.ajudaqui.authenticationms.exception.BadRequestException;
import com.ajudaqui.authenticationms.exception.NotFoundException;
import com.ajudaqui.authenticationms.repository.TokenRepository;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

  final private TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public String createToken(Long userId) {
    LocalDateTime expiration = ofInstant(now().plusMillis(360000),
        ZoneId.of("America/Sao_Paulo"));
    Token token = tokenRepository.save(new Token(generateToken(), userId, expiration));
    if (token.getId() == null)
      throw new BadRequestException("Token não criado");
    return token.getToken();
  }

  public Token findByToken(String token) {
    return tokenRepository.findByToken(token)
        .orElseThrow(() -> new NotFoundException("Token não localizado"));
  }

  @Transactional
  public void delete(String token) {
    tokenRepository.deleteByToken(token);
  }

  private static String generateToken() {
    return String.format("%06d", new SecureRandom().nextInt(1_000_000));
  }
}
