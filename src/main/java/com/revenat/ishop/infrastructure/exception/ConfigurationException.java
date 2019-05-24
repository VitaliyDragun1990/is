package com.revenat.ishop.infrastructure.exception;

import com.revenat.ishop.infrastructure.exception.base.ApplicationException;

/**
 * SIgnals about incorrect configuration settings/parameters
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
