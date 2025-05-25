package com.tfg.nxtlevel.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg.nxtlevel.dto.AuthRequest;
import com.tfg.nxtlevel.dto.RegisterRequest;
import com.tfg.nxtlevel.dto.TokenResponse;
import com.tfg.nxtlevel.persistence.entities.Token;
import com.tfg.nxtlevel.persistence.entities.Token.TokenType;
import com.tfg.nxtlevel.persistence.entities.User;
import com.tfg.nxtlevel.persistence.repositories.TokenRepository;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;

/**
 * Servicio de la autenticación del usuario
 */
@Service
public class AuthService {

	@Autowired
	private UserRepository repository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	/**
	 * Método para el registro del usuario
	 * 
	 * @param request
	 * @return new TokenResponse(jwtToken, refreshToken);
	 */
	public ResponseEntity<?> register(final RegisterRequest request) {
		final User user = new User(request.getName(), request.getEmail(),
				passwordEncoder.encode(request.getPassword()));

		final User savedUser = repository.save(user);
		final String jwtToken = jwtService.generateToken(savedUser);
		final String refreshToken = jwtService.generateRefreshToken(savedUser);

		Map<String, Object> response = new HashMap<>();
		response.put("name", user.getName());
		response.put("message", "Registro exitoso");
		response.put("refresh_token", refreshToken);
		response.put("access_token", jwtToken);

		return ResponseEntity.ok(response);
	}

	/**
	 * Método para el login del usuario
	 * 
	 * @param request
	 * @return new TokenResponse(accessToken, refreshToken);
	 */
	public ResponseEntity<?> authenticate(final AuthRequest request) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			System.out.println("Autenticación Exitosa");
		} catch (Exception e) {
			System.out.println("Error de autenticación: " + e.getMessage());
			throw e;
		}

		final User user = repository.findByEmail(request.getEmail()).orElseThrow();
		final String accessToken = jwtService.generateToken(user);
		final String refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, accessToken);

		Map<String, Object> response = new HashMap<>();
		response.put("refresh_token", refreshToken);
		response.put("access_token", accessToken);
		response.put("name", user.getName());

		return ResponseEntity.ok(response);
	}

	/**
	 * Método que crea el token
	 * 
	 * @param user
	 * @param jwtToken
	 */
	private void saveUserToken(User user, String jwtToken) {
		Token token = new Token();
		token.setUser(user);
		token.getTokenType();
		token.setTokenType(TokenType.BEARER);
		token.setToken(jwtToken);
		token.setRevoked(false);
		token.setExpired(false);

		tokenRepository.save(token);
	}

	/**
	 * Cambia el expired y revoked a true cuando se logea el usuario
	 * 
	 * @param user
	 */
	private void revokeAllUserTokens(final User user) {
		final List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (!validUserTokens.isEmpty()) {
			validUserTokens.forEach(token -> {
				token.setExpired(true);
				token.setRevoked(true);
			});
			tokenRepository.saveAll(validUserTokens);
		}
	}

	/**
	 * Método para refrescar el token
	 * 
	 * @param authentication
	 * @return TokenResponse(accessToken, refreshToken)
	 */
	public TokenResponse refreshToken(@NotNull final String authentication) {

		// Comprueba si la autenticación es nula o no es Bearer
		if (authentication == null || !authentication.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Invalid auth header");
		}
		final String refreshToken = authentication.substring(7);
		final String userEmail = jwtService.extractUsername(refreshToken);
		// Comprueba si el email es nulo
		if (userEmail == null) {
			throw new IllegalArgumentException("Invalid email");
		}

		final User user = this.repository.findByEmail(userEmail).orElseThrow();
		final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
		// Comprueba si el token es válido
		if (!isTokenValid) {
			throw new IllegalArgumentException("Invalid token");
		}

		final String accessToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, accessToken);

		return new TokenResponse(accessToken, refreshToken);
	}
}