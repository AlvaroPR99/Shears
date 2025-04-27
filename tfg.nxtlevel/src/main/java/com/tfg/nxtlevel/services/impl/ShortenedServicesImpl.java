package com.tfg.nxtlevel.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tfg.nxtlevel.persistence.entities.ShortenedURL;
import com.tfg.nxtlevel.persistence.entities.User;
import com.tfg.nxtlevel.persistence.repositories.ShortenedRepository;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;
import com.tfg.nxtlevel.services.inter.ShortenedServices;

/**
 * Servicio del acortador de URL
 */
@Service
public class ShortenedServicesImpl implements ShortenedServices {

	/**
	 * Inyectamos las dependencias
	 */
	@Autowired
	private ShortenedRepository shortenedRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Base que tendrán todas la URL
	 */
	private static final String BASE_URL = "https://Shears.com/";

	/**
	 * Acorta la URL mediante la URL original que se le muestra
	 */
	public String shortUrl(String originalUrl) {
		if (shortenedRepository.existsByOriginalUrl(originalUrl)) {
			// Si ya existe la URL te trae la URL acortada
			return shortenedRepository.findByShortUrl(originalUrl).get().getShortUrl();
		}

		// Autenticación del usuario
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		// Obtención del user para guardar las url acortadas en su usuario
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// Se genera la url acortada
		String shortUrl = BASE_URL + generatedShortUrl();

		// Creamos un nuevo objeto para guardarlo en la bd
		ShortenedURL shortenedUrl = new ShortenedURL();
		shortenedUrl.setOriginalUrl(originalUrl);
		shortenedUrl.setShortUrl(shortUrl);
		shortenedUrl.setDateCreated(System.currentTimeMillis());
		shortenedUrl.setUserUrl(user);

		shortenedRepository.save(shortenedUrl);
		// Devolvemos la URL acortada con la base insertada
		return BASE_URL + shortUrl;
	}

	/**
	 * Acorta la URL pero personalizada por el usuario
	 * 
	 * @param originalUrl
	 * @param customUrl
	 * @return BASE_URL + shortUrl;
	 */
	public String shortUrlCustom(String originalUrl, String customUrl) {
		if (shortenedRepository.existsByOriginalUrl(originalUrl)) {
			// Si ya existe la URL te trae la URL acortada
			return shortenedRepository.findByShortUrl(originalUrl).get().getShortUrl();
		}

		// Autenticación del usuario
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		// Obtención del user para guardar las url acortadas en su usuario
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String shortUrl = BASE_URL + generatedCustomShortUrl(customUrl);

		// Creamos un nuevo objeto para guardarlo en la bd
		ShortenedURL shortenedUrl = new ShortenedURL();
		shortenedUrl.setOriginalUrl(originalUrl);
		shortenedUrl.setShortUrl(shortUrl);
		shortenedUrl.setDateCreated(System.currentTimeMillis());
		shortenedUrl.setUserUrl(user);

		shortenedRepository.save(shortenedUrl);
		// Devolvemos la URL acortada con la base insertada
		return BASE_URL + shortUrl;
	}

	/**
	 * Se obtiene la URL original desde la URL acortada
	 */
	public Optional<String> getOriginalUrl(String shortUrl) {
		return shortenedRepository.findByShortUrl(shortUrl).map(ShortenedURL::getOriginalUrl);
	}

	/**
	 * Genera un URL acortada aleatoriamente
	 */
	public String generatedShortUrl() {
		// Todos los caracteres posibles de
		// TODO: Hay que cambiarlo por una variable
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		// Se crea un objeto. Este objeto devueve la URL acortada
		StringBuilder code = new StringBuilder();

		for (int i = 0; i <= 6; i++) {
			code.append(chars.charAt(random.nextInt(chars.length())));
		}

		// Se parsea el objeto y se convierte a String
		return code.toString();
	}

	/**
	 * Genera una URL customizada
	 */
	public String generatedCustomShortUrl(String custom) {
		if (custom == null || custom.isEmpty()) {
			throw new IllegalArgumentException("El código no puede estar vacío");
		}
		// TODO: Cambiar la validación (Solo existe si ese usuario creó una igual)
		if (shortenedRepository.existsByShortUrl(custom)) {
			throw new IllegalArgumentException("Ya existe esta URL");
		}
		return custom;
	}

	/**
	 * Busca en concreto una url del usuario
	 * 
	 * @param shortUrl
	 * @return
	 */
	public ShortenedURL getUserUrl(String shortUrl) {

		// Obtiene el email
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		return shortenedRepository.findByShortUrlAndUserUrl(shortUrl, user)
				.orElseThrow(() -> new RuntimeException("Short URL not found for the current user"));
	}

	/**
	 * Obtiene todas las url del usuario
	 * 
	 * @return List<ShortenedURL>
	 */
	public List<ShortenedURL> getAllUserUrl() {
		// Obtiene el email
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		return shortenedRepository.findAllByUserUrl(user);
	}

}
