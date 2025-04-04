package com.tfg.nxtlevel.persistence.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entidad de los usuarios
 */
@Entity
@Table(name = "T_USER")
public class User {

	/**
	 * ID del usuarios
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "C_USERID")
	private Long id;

	/**
	 * Nombre del usuario
	 */
	@Column(name = "C_NAME")
	private String name;

	/**
	 * Email del usuario
	 */
	@Column(name = "C_EMAIL", unique = true)
	private String email;

	/**
	 * Contraseña del usuario
	 */
	@Column(name = "C_PASSWORD", nullable = false)
	private String password;

	/**
	 * Conexión con token
	 */
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Token> tokens;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

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

	/**
	 * @return the tokens
	 */
	public List<Token> getTokens() {
		return tokens;
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * @param name
	 * @param email
	 * @param password
	 */
	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

}
