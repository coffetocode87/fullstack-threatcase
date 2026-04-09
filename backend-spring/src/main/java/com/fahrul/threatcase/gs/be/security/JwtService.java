package com.fahrul.threatcase.gs.be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

	private static final String SECRET = "supersecretkeysupersecretkeysupersecretkeysupersecretkey";

	private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

	// ===============================
	// GENERATE ACCESS TOKEN
	// ===============================
	public String generateToken(String username, String role) {

		return Jwts.builder().subject(username).claim("role", role).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 jam
				.signWith(key).compact();
	}

	// ===============================
	// GENERATE REFRESH TOKEN
	// ===============================
	public String generateRefreshToken(String username) {

		return Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + 604800000)) // 7 hari
				.signWith(key).compact();
	}

	// ===============================
	// PARSE TOKEN (DIGUNAKAN SEMUA METHOD)
	// ===============================
	private Claims parseToken(String token) {

		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	// ===============================
	// EXTRACT USERNAME
	// ===============================
	public String extractUsername(String token) {

		return parseToken(token).getSubject();
	}

	// ===============================
	// EXTRACT ROLE
	// ===============================
	public String extractRole(String token) {

		return parseToken(token).get("role", String.class);
	}

	// ===============================
	// VALIDATE TOKEN
	// ===============================
	public boolean isTokenValid(String token) {

		try {

			parseToken(token);

			return true;

		} catch (Exception e) {

			return false;
		}
	}
}