package com.ajudaqui.authenticationms.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ajudaqui.authenticationms.security.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;

	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generatedJwtToken(Authentication authentication) {
		UserDetailsImpl userDetail = (UserDetailsImpl) authentication.getPrincipal();

		LocalDateTime issuedAt = LocalDateTime.now(ZoneId.systemDefault());
		Date issuedAtDate = Date.from(issuedAt.atZone(ZoneId.systemDefault()).toInstant());

		LocalDateTime expirationDateTime = issuedAt.plus(jwtExpirationMs, ChronoUnit.MILLIS);
		Date expirationDate = Date.from(expirationDateTime.atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder().setSubject(userDetail.getUsername()).setIssuedAt(issuedAtDate)
				.setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

//		Jwts.builder()
//		.setSubject((userDetail.getUsername()))
//		.setIssuedAt(new Date())
//		.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//		.signWith(SignatureAlgorithm.HS512, jwtSecret)
//		.compact();

	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
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
