package com.revenat.ishop.infrastructure.exception.base;

/**
 * Parent class for all application specific exceptions.
 * 
 * @author Vitaly Dragun
 *
 */
public abstract class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -7984574382154439083L;
	private final int code;

	public ApplicationException(String message, Throwable cause, int statusCode) {
		super(message, cause);
		this.code = statusCode;
	}

	public ApplicationException(String message, int statusCode) {
		super(message);
		this.code = statusCode;
	}
	
	public final int getCode() {
		return code;
	}
}
