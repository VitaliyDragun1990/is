package com.revenat.ishop.infrastructure.exception;

import com.revenat.ishop.infrastructure.exception.base.ApplicationException;

/**
 * Signals about data access layer unexpected situations
 * 
 * @author Vitaly Dragun
 *
 */
public class PersistenceException extends ApplicationException {
	private static final long serialVersionUID = -3176677130267805185L;
	
	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
