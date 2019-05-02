package com.revenat.ishop.exception;

/**
 * Custom exception that represents validation error of user provided data.
 * 
 * @author Vitaly Dragun
 *
 */
public class ValidationException extends ApplicationException {
	private static final long serialVersionUID = 6176636234085711406L;

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(String message) {
		super(message);
	}

}
