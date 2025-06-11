package com.tfg.nxtlevel.dto;

public class ShortenedURLDTO {

	private String originalUrl;
	private String shortUrl;
	private int accessCount;
	private Long dateCreated;

	public ShortenedURLDTO() {
		// Constructor vacío
	}

	public ShortenedURLDTO(String originalUrl, String shortUrl, int accessCount, Long dateCreated) {
		this.originalUrl = originalUrl;
		this.shortUrl = shortUrl;
		this.accessCount = accessCount;
		this.dateCreated = dateCreated;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public Long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Long dateCreated) {
		this.dateCreated = dateCreated;
	}
}
