package com.tfg.nxtlevel.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Clase que maneja las Exceptions de la autentificación
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handlerArgumentExcepction(IllegalArgumentException exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handlerRuntimeException(RuntimeException exception) {
		return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDuplicateKey(DataIntegrityViolationException ex) {
		if (ex.getMessage().contains("Duplicate entry")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor.");
	}
}
