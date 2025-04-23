package com.tfg.nxtlevel.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tfg.nxtlevel.persistence.entities.User;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;

/**
 * Clase configuración
 */
@Configuration
public class AppConfig {

	@Autowired
	private UserRepository repository;

	/**
	 * Bean de UserDetails -> Encuentra al usuario por email
	 * 
	 * @return
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> {
			final User user = repository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			return org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
					.password(user.getPassword()).authorities(new ArrayList<>()).build();
		};
	}

	/**
	 * Bean para la autenticación del usuario
	 * 
	 * @return authProvider
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Encripta la contraseña
	 * 
	 * @return BCryptPasswordEncoder()
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}