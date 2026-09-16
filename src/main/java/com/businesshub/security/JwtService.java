package com.businesshub.security;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String secretKey = "businesshub-secret-key-businesshub-secret-key";

	public String generateToken(String email) {

		return Jwts.builder().subject(email).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes())).compact();
	}

	public String extractEmail(String token) {

		return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build().parseSignedClaims(token)
				.getPayload().getSubject();
	}

	public boolean isTokenValid(String token, String email) {

		String extractedEmail = extractEmail(token);

		return extractedEmail.equals(email) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {

		Date expiration = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
				.parseSignedClaims(token).getPayload().getExpiration();

		return expiration.before(new Date());
	}
}