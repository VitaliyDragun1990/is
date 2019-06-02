package com.revenat.ishop.infrastructure.exception.base;

/**
 * Parent class for all application specific exceptions.
 * 
 * @author Vitaly Dragun
 *
 */
public abstract class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -7984574382154439083L;

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}
}
