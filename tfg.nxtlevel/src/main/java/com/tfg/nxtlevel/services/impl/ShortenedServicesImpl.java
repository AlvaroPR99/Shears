package com.tfg.nxtlevel.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tfg.nxtlevel.dto.ShortenedURLDTO;
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
		return shortUrl;
	}

	/**
	 * Acorta la URL pero personalizada por el usuario
	 * 
	 * @param originalUrl
	 * @param customUrl
	 * @return BASE_URL + shortUrl;
	 */
	public String shortUrlCustom(String originalUrl, String customUrl) {

		// Autenticación del usuario
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		// Obtención del user para guardar las url acortadas en su usuario
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		String shortUrl = BASE_URL + generatedCustomShortUrl(customUrl);

		// Valida si el usuario tiene otra url con el mismo nombre
		if (shortenedRepository.existsByShortUrlAndUserUrl(shortUrl, user)) {
			throw new IllegalArgumentException("Ya existe esta URL");
		}

		// Creamos un nuevo objeto para guardarlo en la bd
		ShortenedURL shortenedUrl = new ShortenedURL();
		shortenedUrl.setOriginalUrl(originalUrl);
		shortenedUrl.setShortUrl(shortUrl);
		shortenedUrl.setDateCreated(System.currentTimeMillis());
		shortenedUrl.setUserUrl(user);

		shortenedRepository.save(shortenedUrl);
		// Devolvemos la URL acortada con la base insertada
		return shortUrl;
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
		// Obtiene el email
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

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
	public List<ShortenedURLDTO> getAllUserUrl() {
		// Obtiene el email
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		// Busca todos las url de ese email desde el dto
		return shortenedRepository.findAllByUserUrl(user).stream()
				.map(url -> new ShortenedURLDTO(url.getOriginalUrl(), url.getShortUrl())).collect(Collectors.toList());
	}

	/**
	 * Generera el qr
	 * 
	 * @param text
	 * @param width
	 * @param height
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	public byte[] generateQR(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

		return pngOutputStream.toByteArray();
	}

}
