package com.tfg.nxtlevel.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.tfg.nxtlevel.persistence.entities.Token;
import com.tfg.nxtlevel.persistence.repositories.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JWTAuthenticationFilter jwtAuthFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	private TokenRepository tokenRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors() // <<--- HABILITAMOS CORS AQUÍ
				.and()
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/api/auth/**", "/api/auth/login", "/api/v1/shorten", "/Shears/s/**",
								"/Shears/shorten/**", "/Shears/shorten", "/Shears/delete-url/{shortUrl}")
						.permitAll().anyRequest().authenticated())
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:4200"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	private void logout(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		final String jwt = authHeader.substring(7);
		final Token storedToken = tokenRepository.findByToken(jwt).orElse(null);
		if (storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			tokenRepository.save(storedToken);
			SecurityContextHolder.clearContext();
		}
	}
}
