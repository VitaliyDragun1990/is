package com.revenat.ishop.exception.base;

/**
 * Parent class for all application specific exceptions.
 * 
 * @author Vitaly Dragun
 *
 */
public abstract class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -7984574382154439083L;
	private final int statusCode;

	public ApplicationException(String message, Throwable cause, int statusCode) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public ApplicationException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
	
	public final int getStatusCode() {
		return statusCode;
	}
}
