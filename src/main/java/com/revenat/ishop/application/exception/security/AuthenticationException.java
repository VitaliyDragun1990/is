package com.revenat.ishop.application.exception.security;

import com.revenat.ishop.domain.exception.base.ApplicationException;

/**
 * Signals some kind of error during client authentication
 * process.
 * 
 * @author Vitaly Dragun
 *
 */
public class AuthenticationException extends ApplicationException {
	private static final long serialVersionUID = 25477902049903710L;

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AuthenticationException(String message) {
		super(message);
	}
}
