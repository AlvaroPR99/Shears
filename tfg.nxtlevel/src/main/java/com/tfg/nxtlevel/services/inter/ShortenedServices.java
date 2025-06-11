package com.tfg.nxtlevel.services.inter;

import java.util.Optional;

import com.tfg.nxtlevel.dto.UserRequest;

public interface ShortenedServices {

	/**
	 * Acorta la URL generando un código aleatorio
	 * 
	 * @param originalUrl
	 * @return String
	 */
	String shortUrl(String originalUrl);

	/**
	 * Acorta la URL generando un código personalizado por el usuario
	 * 
	 * @param originalUrl
	 * @param customUrl
	 * @return String
	 */
	String shortUrlCustom(String originalUrl, String customUrl);

	/**
	 * Obtiene la URL original desde la URL corta
	 * 
	 * @param shortUrl
	 * @return String
	 */
	Optional<String> getOriginalUrl(String shortUrl);

	/**
	 * Genera una URL corta aletoriamente
	 * 
	 * @return String
	 */
	String generatedShortUrl();

	/**
	 * Genera una URL customizada por el usuario
	 * 
	 * @param custom
	 * @return
	 */
	String generatedCustomShortUrl(String custom);

	/**
	 * Incrementa el contador
	 * 
	 * @param shortenedUrl
	 */
	void incrementAccessCount(String shortenedUrl);

	/**
	 * Devulve datos del usuario
	 * 
	 * @return
	 */
	Optional<UserRequest> getUserData();
}
