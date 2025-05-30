package com.tfg.nxtlevel.dto;

/**
 * DTO de los datos para el login
 */
public class LoginRequest {

	/**
	 * Email del usuario
	 */
	private String email;

	/**
	 * Contraseña del usuario
	 */
	private String password;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
