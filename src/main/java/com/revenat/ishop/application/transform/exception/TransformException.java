package com.revenat.ishop.application.transform.exception;

/**
 * Represents some kind of exception in transformation flow of execution.
 * 
 * @author Vitaly Dragun
 *
 */
public class TransformException extends RuntimeException {
	private static final long serialVersionUID = -2785915886021558144L;

	public TransformException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransformException(Throwable cause) {
		super(cause);
	}

	public TransformException(String message) {
		super(message);
	}
}
