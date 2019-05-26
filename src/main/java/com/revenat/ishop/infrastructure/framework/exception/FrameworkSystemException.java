package com.revenat.ishop.infrastructure.framework.exception;

/**
 * Custom framework exception.
 * 
 * @author Vitaly Dragun
 *
 */
public class FrameworkSystemException extends RuntimeException {
	private static final long serialVersionUID = 4331634956574449403L;

	public FrameworkSystemException(String message) {
		super(message);
	}

	public FrameworkSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public FrameworkSystemException(Throwable cause) {
		super(cause);
	}

}
