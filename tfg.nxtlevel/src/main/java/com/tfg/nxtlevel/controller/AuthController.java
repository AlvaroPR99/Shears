package com.tfg.nxtlevel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.nxtlevel.dto.AuthRequest;
import com.tfg.nxtlevel.dto.RegisterRequest;
import com.tfg.nxtlevel.dto.TokenResponse;
import com.tfg.nxtlevel.security.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService service;

	@PostMapping("/register")
	public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
		final TokenResponse response = service.register(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
		final TokenResponse response = service.authenticate(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/refresh-token")
	public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication) {
		return service.refreshToken(authentication);
	}

}