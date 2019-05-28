package com.revenat.ishop.infrastructure.framework.exception;

public class FrameworkPersistenceException extends FrameworkSystemException {
	private static final long serialVersionUID = 8098384041081871146L;

	public FrameworkPersistenceException(Throwable cause) {
		super(cause);
	}

	public FrameworkPersistenceException(String message) {
		super(message);
	}

}
