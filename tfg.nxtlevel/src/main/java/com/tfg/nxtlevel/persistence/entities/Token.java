package com.tfg.nxtlevel.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad token
 */
@Entity
@Table(name = "T_TOKEN")
public class Token {

	/**
	 * Tipos de tokens
	 */
	public enum TokenType {
		BEARER
	}

	/**
	 * ID del token
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Token
	 */
	@Column(name = "C_TOKEN", unique = true)
	private String token;

	/**
	 * Tipo de token
	 */
	@Column(name = "C_TOKENTYPE")
	@Enumerated(EnumType.STRING)
	private TokenType tokenType = TokenType.BEARER;

	/**
	 * Estado del token
	 */
	private boolean revoked;

	/**
	 * Estado del token
	 */
	private boolean expired;

	/**
	 * Conexión con el usuario
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "C_USERID")
	public User user;

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
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the tokenType
	 */
	public TokenType getTokenType() {
		return tokenType;
	}

	/**
	 * @param tokenType the tokenType to set
	 */
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * @return the revoked
	 */
	public boolean isRevoked() {
		return revoked;
	}

	/**
	 * @param revoked the revoked to set
	 */
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}

	/**
	 * @return the expired
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @param expired the expired to set
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Constructor
	 * 
	 * @param token
	 * @param tokenType
	 * @param revoked
	 * @param expired
	 * @param user
	 */
	public Token(String token, TokenType tokenType, boolean revoked, boolean expired, User user) {
		super();
		this.token = token;
		this.tokenType = tokenType;
		this.revoked = revoked;
		this.expired = expired;
		this.user = user;
	}

	/**
	 * Constructor por defecto
	 */
	public Token() {
		super();
	}

}
