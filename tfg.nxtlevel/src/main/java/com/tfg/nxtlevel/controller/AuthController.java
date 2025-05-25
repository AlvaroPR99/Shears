package com.tfg.nxtlevel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.nxtlevel.dto.AuthRequest;
import com.tfg.nxtlevel.dto.RegisterRequest;
import com.tfg.nxtlevel.dto.TokenResponse;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;
import com.tfg.nxtlevel.security.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200/**")
public class AuthController {

	@Autowired
	private AuthService service;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya está registrado");
		}
		final ResponseEntity<?> response = service.register(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
		final ResponseEntity<?> response = service.authenticate(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh-token")
	public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication) {
		return service.refreshToken(authentication);
	}

}