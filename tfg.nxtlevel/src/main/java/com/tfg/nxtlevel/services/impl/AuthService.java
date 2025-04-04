package com.tfg.nxtlevel.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg.nxtlevel.dto.RegisterRequest;
import com.tfg.nxtlevel.dto.TokenResponse;
import com.tfg.nxtlevel.persistence.entities.User;
import com.tfg.nxtlevel.persistence.repositories.TokenRepository;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;

@Service
public class AuthService {

	/**
	 * Inyectamos la dependencias
	 */

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;

	public TokenResponse register(RegisterRequest request) {
		User user = new User(request.getName(), request.getEmail(), passwordEncoder.encode(request.getPassword()));
		User saveUser = userRepository.save(user);
		String jwtToken = jwtService.generateToken(saveUser);
		String refreshToken = jwtService.generateRefreshToken(saveUser);

		return new TokenResponse(jwtToken, refreshToken);
	}

}
