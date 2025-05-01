package com.tfg.nxtlevel.dto;

/**
 * DTO de las url acortadas
 */
public class ShortenedURLDTO {

	private String originalUrl;
	private String shortUrl;

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
	 * @param originalUrl
	 * @param shortUrl
	 */
	public ShortenedURLDTO(String originalUrl, String shortUrl) {
		super();
		this.originalUrl = originalUrl;
		this.shortUrl = shortUrl;
	}

	/**
	 * 
	 */
	public ShortenedURLDTO() {
		super();
	}

}
