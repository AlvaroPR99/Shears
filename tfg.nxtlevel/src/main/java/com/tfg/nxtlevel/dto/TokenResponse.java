package com.tfg.nxtlevel.dto;

/**
 * Clase para enviar el token como respuesta
 */
public class TokenResponse {

	private String jwtToken;

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
