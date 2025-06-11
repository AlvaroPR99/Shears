package com.tfg.nxtlevel.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Clase entidad para el acortador de URL
 */
@Entity
@Table(name = "T_SHORTENED_URL", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "C_SHORT_URL", "C_USER_URL" }) })
public class ShortenedURL {

	/**
	 * ID PK
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * URL original
	 */
	@Column(name = "C_ORIGINAL_URL", nullable = false, length = 1000)
	private String originalUrl;

	/**
	 * URL acortada
	 */
	@Column(name = "C_SHORT_URL", nullable = false, unique = true)
	private String shortUrl;

	/**
	 * Conexión con el usuario
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_USER_URL")
	public User userUrl;
	/**
	 * Fecha de creación de la URL acortada
	 */
	@Column(name = "C_DATE_CREATED", nullable = false)
	private Long dateCreated = System.currentTimeMillis();

	/**
	 * Contador de accesos a la URL
	 */
	@Column(name = "C_ACCESS_COUNT", nullable = false)
	private int accessCount = 0;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the userUrl
	 */
	public User getUserUrl() {
		return userUrl;
	}

	/**
	 * @param userUrl the userUrl to set
	 */
	public void setUserUrl(User userUrl) {
		this.userUrl = userUrl;
	}

	/**
	 * @return the originalUrl
	 */
	public String getOriginalUrl() {
		return originalUrl;
	}

	/**
	 * @param originalUrl the originalUrl to set
	 */
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	/**
	 * @return the shortUrl
	 */
	public String getShortUrl() {
		return shortUrl;
	}

	/**
	 * @param shortUrl the shortUrl to set
	 */
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	/**
	 * @return the dateCreated
	 */
	public Long getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Long dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param originalUrl
	 * @param shortUrl
	 * @param dateCreated
	 */
	public ShortenedURL(Long id, String originalUrl, String shortUrl, Long dateCreated) {
		super();
		this.id = id;
		this.originalUrl = originalUrl;
		this.shortUrl = shortUrl;
		this.dateCreated = dateCreated;
	}

	/**
	 * Constructor por defecto
	 */
	public ShortenedURL() {
		super();
	}

	public ShortenedURL get() {

		return null;
	}

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public void incrementAccessCount() {
		this.accessCount++; // Método que incrementa el contador
	}

}
