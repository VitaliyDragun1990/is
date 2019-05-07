package com.revenat.ishop.exception;

import com.revenat.ishop.exception.base.ApplicationException;

/**
 * This custom exception represents error related to configuration process.
 * 
 * @author Vitaly Dragun
 *
 */
public class ConfigurationException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message, 500);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause, 500);
	}
}
