package com.revenat.ishop.application.infra.exception;

import com.revenat.ishop.application.infra.exception.base.ApplicationException;

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
