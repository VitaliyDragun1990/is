package com.revenat.ishop.exception;

import com.revenat.ishop.exception.base.ApplicationException;

/**
 * This exception represents some kind of error during user authentication
 * process.
 * 
 * @author Vitaly Dragun
 *
 */
public class AuthenticationException extends ApplicationException {
	private static final long serialVersionUID = 25477902049903710L;

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause, 401);
	}
}
