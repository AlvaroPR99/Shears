package com.tfg.nxtlevel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.nxtlevel.dto.RegisterRequest;
import com.tfg.nxtlevel.dto.TokenResponse;
import com.tfg.nxtlevel.services.impl.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	@Autowired
	private AuthService service;

	@PostMapping("/register")
	public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request) {
		final TokenResponse token = service.register(request);
		return ResponseEntity.ok(token);
	}

}
