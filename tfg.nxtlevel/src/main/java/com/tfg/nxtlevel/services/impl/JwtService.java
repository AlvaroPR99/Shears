package com.tfg.nxtlevel.services.impl;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tfg.nxtlevel.persistence.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Autowired
	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	@Autowired
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;

	@Autowired
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshTokenExpiration;

	/**
	 * Método para generar el token
	 * 
	 * @param user
	 * @return generateToken
	 */
	public String generateToken(final User user) {
		return buildToken(user, jwtExpiration);
	}

	/**
	 * Método para generar el token de refresco
	 * 
	 * @param user
	 * @return generateRefreshToken
	 */
	public String generateRefreshToken(final User user) {
		return buildToken(user, refreshTokenExpiration);
	}

	/**
	 * Método para construir el token
	 * 
	 * @param user
	 * @param expiration
	 * @return String
	 */
	private String buildToken(final User user, final long expiration) {
		return Jwts.builder().setId(user.getId().toString()).setClaims(Map.of("name", user.getName()))
				.setSubject(user.getEmail()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSingInKey()).compact();
	}

	/**
	 * Método para decodificar la contraseña
	 * 
	 * @return SecretKey
	 */
	private SecretKey getSingInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
