package com.revenat.ishop.exception;

/**
 * Custom exception which represents error related to data storage operations.
 * 
 * @author Vitaly Dragun
 *
 */
public class DataStorageException extends ApplicationException {
	private static final long serialVersionUID = -3176677130267805185L;
	
	public DataStorageException(String message) {
		super(message);
	}

	public DataStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
