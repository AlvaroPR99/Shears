package com.tfg.nxtlevel.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.nxtlevel.persistence.entities.ShortenedURL;

/**
 * Repositorio de la entidad ShortenedURL
 */
@Repository
public interface ShortenedRepository extends JpaRepository<ShortenedURL, Long> {

	/**
	 * Busca en la BD una url corta
	 * 
	 * @param shortUrl
	 * @return ShortenedURL
	 */
	Optional<ShortenedURL> findByShortUrl(String shortUrl);

	/**
	 * Comprueba si existe la URL ya ha sido cortada
	 * 
	 * @param originalUrl
	 * @return
	 */
	boolean existsByOriginalUrl(String originalUrl);

	boolean existsByShortUrl(String shortUrl);
}
