package com.tfg.nxtlevel.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tfg.nxtlevel.persistence.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	/**
	 * Extrae el email del usuario
	 * 
	 * @param token
	 * @return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody().getSubject()
	 */
	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Genera un token
	 * 
	 * @param user
	 * @return buildToken(user, jwtExpiration)
	 */
	public String generateToken(final User user) {
		return buildToken(user, jwtExpiration);
	}

	/**
	 * Genera un token de refresco
	 * 
	 * @param user
	 * @return buildToken(user, refreshExpiration)
	 */
	public String generateRefreshToken(final User user) {
		return buildToken(user, refreshExpiration);
	}

	/**
	 * Construye el token
	 * 
	 * @param user
	 * @param expiration
	 * @return Jwts.builder().setClaims(Map.of("name",
	 *         user.getName())).setSubject(user.getEmail()) .setIssuedAt(new
	 *         Date(System.currentTimeMillis())) .setExpiration(new
	 *         Date(System.currentTimeMillis() +
	 *         expiration)).signWith(getSignInKey()).compact();
	 */
	private String buildToken(final User user, final long expiration) {
		return Jwts.builder().setClaims(Map.of("name", user.getName())).setSubject(user.getEmail())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSignInKey()).compact();
	}

	/**
	 * Comprueba si el token es válido
	 * 
	 * @param token
	 * @param user
	 * @return (username.equals(user.getEmail())) && !isTokenExpired(token)
	 */
	public boolean isTokenValid(String token, User user) {
		final String username = extractUsername(token);
		return (username.equals(user.getEmail())) && !isTokenExpired(token);
	}

	/**
	 * Comprueba si ha expirado el token
	 * 
	 * @param token
	 * @return extractExpiration(token).before(new Date())
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Devuelve la fecha de expiración del token
	 * 
	 * @param token
	 * @return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody()
	 *         .getExpiration();
	 */
	private Date extractExpiration(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody()
				.getExpiration();
	}

	/**
	 * Secret key del token
	 * 
	 * @return Keys.hmacShaKeyFor(keyBytes)
	 */
	private SecretKey getSignInKey() {
		final byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}