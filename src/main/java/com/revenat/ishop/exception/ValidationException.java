package com.revenat.ishop.exception;

/**
 * This custom exception represents violation of some kind of validation rules.
 * 
 * @author Vitaly Dragun
 *
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message);
	}
}
