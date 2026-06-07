package com.ajudaqui.porteiro.repository;

import java.util.Optional;

import com.ajudaqui.porteiro.entity.Token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

  Optional<Token> findByToken(String token);

  void deleteByToken(String token);
}
