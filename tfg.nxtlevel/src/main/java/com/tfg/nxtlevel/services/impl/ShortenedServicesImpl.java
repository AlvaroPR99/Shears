package com.tfg.nxtlevel.services.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.nxtlevel.persistence.entities.ShortenedURL;
import com.tfg.nxtlevel.persistence.repositories.ShortenedRepository;
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

		String shortUrl = generatedShortUrl();

		// Creamos un nuevo objeto para guardarlo en la bd
		ShortenedURL shortenedUrl = new ShortenedURL(null, originalUrl, shortUrl, System.currentTimeMillis());
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

		String shortUrl = generatedCustomShortUrl(customUrl);

		// Creamos un nuevo objeto para guardarlo en la bd
		ShortenedURL shortenedUrl = new ShortenedURL(null, originalUrl, shortUrl, System.currentTimeMillis());
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
		if (shortenedRepository.existsByShortUrl(custom)) {
			throw new IllegalArgumentException("Ya existe esta URL");
		}
		return custom;
	}

}
