package com.revenat.ishop.domain.exception;

import com.revenat.ishop.domain.exception.base.ApplicationException;

/**
 * Signals about incorrect configuration settings/parameters
 * 
 * @author Vitaly Dragun
 *
 */
public class ConfigurationException extends ApplicationException {
	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
