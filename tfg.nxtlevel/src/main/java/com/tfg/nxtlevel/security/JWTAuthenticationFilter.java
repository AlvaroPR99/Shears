package com.tfg.nxtlevel.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tfg.nxtlevel.persistence.entities.User;
import com.tfg.nxtlevel.persistence.repositories.TokenRepository;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private UserRepository userRepository;

	public JWTAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
			TokenRepository tokenRepository, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
		this.tokenRepository = tokenRepository;
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String path = request.getServletPath();
		if (path.startsWith("/api/auth")) {
			// Permite el acceso sin JWT
			filterChain.doFilter(request, response);
			return;
		}

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String jwt = authHeader.substring(7);
		final String userEmail = jwtService.extractUsername(jwt);
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (userEmail == null || authentication != null) {
			filterChain.doFilter(request, response);
			return;
		}

		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
		final boolean isTokenExpiredOrRevoked = tokenRepository.findByToken(jwt)
				.map(token -> !token.isExpired() && !token.isRevoked()).orElse(false);

		if (isTokenExpiredOrRevoked) {
			final Optional<User> user = userRepository.findByEmail(userEmail);

			if (user.isPresent()) {
				final boolean isTokenValid = jwtService.isTokenValid(jwt, user.get());

				if (isTokenValid) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}

		filterChain.doFilter(request, response);
	}
}