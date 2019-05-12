package com.revenat.ishop.application.infra.exception;

import com.revenat.ishop.application.infra.exception.base.ApplicationException;

/**
 * Signals about data access layer unexpected situations
 * 
 * @author Vitaly Dragun
 *
 */
public class PersistenceException extends ApplicationException {
	private static final long serialVersionUID = -3176677130267805185L;
	
	public PersistenceException(String message) {
		super(message, 500);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause, 500);
	}
}
