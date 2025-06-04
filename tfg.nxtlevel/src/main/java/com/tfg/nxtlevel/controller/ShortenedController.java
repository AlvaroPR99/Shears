package com.tfg.nxtlevel.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tfg.nxtlevel.dto.ShortenedURLDTO;
import com.tfg.nxtlevel.persistence.entities.ShortenedURL;
import com.tfg.nxtlevel.persistence.entities.User;
import com.tfg.nxtlevel.persistence.repositories.ShortenedRepository;
import com.tfg.nxtlevel.persistence.repositories.UserRepository;
import com.tfg.nxtlevel.services.impl.ShortenedServicesImpl;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/Shears")
//Permitir peticiones desde cualquier origen (Habilita el CORS)
@CrossOrigin(origins = "http://localhost:4200/**")
public class ShortenedController {

	@Autowired
	private ShortenedServicesImpl shortenedService;

	@Autowired
	private ShortenedRepository shortenedRepository;

	@Autowired
	private UserRepository userRepository;

	public ShortenedController(ShortenedServicesImpl shortenedService) {
		this.shortenedService = shortenedService;
	}

	@PostMapping("/shorten")
	public Map<String, String> shortenUrl(@RequestBody Map<String, String> request) {

		// Extrae la URL larga del Map.
		String originalUrl = request.get("url");
		// Llama al servicio de acortamiento de URL
		String shortUrl = shortenedService.shortUrl(originalUrl);
		// Crea un objeto JSON con la URL corta y lo devuelve en la respuesta.
		return Map.of("shortUrl", shortUrl);

	}

	@PostMapping("/shorten/{customUrl}")
	public Map<String, String> shortenUrl(@RequestBody Map<String, String> request, @PathVariable String customUrl) {

		// Extrae la URL larga del Map.
		String originalUrl = request.get("url");
		// Llama al servicio de acortamiento de URL
		String shortUrl = shortenedService.shortUrlCustom(originalUrl, customUrl);
		// Crea un objeto JSON con la URL corta y lo devuelve en la respuesta.
		return Map.of("shortUrl", shortUrl);

	}

	/**
	 * Te redirige a la página original de url original
	 * 
	 * @param shortCode
	 * @return
	 */
	@GetMapping("/s/{shortCode}")
	public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortCode) {
		Optional<String> originalUrl = shortenedService.getOriginalUrl(shortCode);

		// Devuelve la solicitud https del original
		return originalUrl.map(url -> ResponseEntity.status(HttpStatus.FOUND).header("Location", url).build())
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	/**
	 * Genera el qr de la url acortada
	 * 
	 * @param shortCode
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/qr/{shortCode}")
	public ResponseEntity<byte[]> getQrCode(@PathVariable String shortCode) throws Exception {
		// Obtener el usuario autenticado
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		String BaseUrl = "http://localhost:8080/Shears/s/";

		// Buscar la URL solo si pertenece al usuario
		ShortenedURL url = shortenedRepository.findByShortUrlAndUserUrl(shortCode, user)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta URL"));

		String completeUrl = BaseUrl + shortCode;

		// Generar el QR
		byte[] qrImage = shortenedService.generateQR(completeUrl, 250, 250);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setContentLength(qrImage.length);

		return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
	}

	// TODO: Mirar si se puede cambiar de controlador
	/**
	 * Método para eliminar usuario
	 * 
	 * @return ResponseEntity
	 */
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser() {
		shortenedService.deleteUser();
		return ResponseEntity.ok("Usuario eliminado correctamente");
	}

	// TODO: Probar en un futuro cuando esté el front

	/**
	 * Método para eliminar la url
	 * 
	 * @param shortUrl
	 * @return
	 */
	@DeleteMapping("/delete-url/{shortUrl}")
	@Transactional
	public ResponseEntity<?> deleteUrl(@PathVariable String shortUrl) {
		Optional<ShortenedURL> urlOptional = shortenedRepository.findByShortUrl(shortUrl);
		if (urlOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL no encontrada con el código: " + shortUrl);
		}
		shortenedRepository.deleteByShortUrl(shortUrl);
		return ResponseEntity.ok("URL eliminada correctamente");
	}

	/**
	 * Método para obtener las url de ese usuario
	 * 
	 * @return List<ShortenedURLDTO>
	 */
	@GetMapping("/get-users")
	public List<ShortenedURLDTO> getUserUrl() {
		return shortenedService.getAllUserUrl();
	}

}
