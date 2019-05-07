package com.revenat.ishop.exception;

import com.revenat.ishop.exception.base.ApplicationException;

/**
 * Custom exception which represents error related to data storage operations.
 * 
 * @author Vitaly Dragun
 *
 */
public class DataStorageException extends ApplicationException {
	private static final long serialVersionUID = -3176677130267805185L;
	
	public DataStorageException(String message) {
		super(message, 500);
	}

	public DataStorageException(String message, Throwable cause) {
		super(message, cause, 500);
	}
}
