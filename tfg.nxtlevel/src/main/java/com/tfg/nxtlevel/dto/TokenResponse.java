package com.tfg.nxtlevel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Clase para enviar el token como respuesta
 */
public class TokenResponse {

	@JsonProperty("access_token")
	private String jwtToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	/**
	 * @return the jwtToken
	 */
	public String getJwtToken() {
		return jwtToken;
	}

	/**
	 * @param jwtToken the jwtToken to set
	 */
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	/**
	 * @return the refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public TokenResponse(String jwtToken, String refreshToken) {
		this.jwtToken = jwtToken;
		this.refreshToken = refreshToken;
	}

}
