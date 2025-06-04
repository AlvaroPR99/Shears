package com.tfg.nxtlevel.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg.nxtlevel.persistence.entities.ShortenedURL;
import com.tfg.nxtlevel.persistence.entities.User;

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
	 * Comprueba si existe la URL original
	 * 
	 * @param originalUrl
	 * @return boolean
	 */
	boolean existsByOriginalUrl(String originalUrl);

	/**
	 * Comprueba si existe la url acortada
	 * 
	 * @param shortUrl
	 * @return boolean
	 */
	boolean existsByShortUrl(String shortUrl);

	/**
	 * Busca todas las url del usuario
	 * 
	 * @param user
	 * @return List<ShortenedURL>
	 */
	List<ShortenedURL> findAllByUserUrl(User user);

	/**
	 * Busca una url en concreto del usuario
	 * 
	 * @param shortUrl
	 * @param user
	 * @return Optional<ShortenedURL>
	 */
	Optional<ShortenedURL> findByShortUrlAndUserUrl(String shortUrl, User user);

	/**
	 * Elimina la url del usuario
	 * 
	 * @param shortUrl
	 * @param user
	 * @return Optional<ShortenedURL>
	 */
	Optional<ShortenedURL> deleteByShortUrlAndUserUrl(ShortenedURL shortUrl, User user);

	/**
	 * Elimina la url
	 * 
	 * @param shortUrl
	 */
	void deleteByShortUrl(String shortUrl);

	/**
	 * Busca si existe esa url corta
	 * 
	 * @param shortUrl
	 * @param user
	 * @return boolean
	 */
	boolean existsByShortUrlAndUserUrl(String shortUrl, User user);
}
