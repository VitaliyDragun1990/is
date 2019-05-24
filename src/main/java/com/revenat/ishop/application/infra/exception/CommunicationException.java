package com.revenat.ishop.application.infra.exception;

import com.revenat.ishop.application.infra.exception.base.ApplicationException;

/**
 * Signals about exceptional cases in the work of external services and API
 * 
 * @author Vitaly Dragun
 *
 */
public class CommunicationException extends ApplicationException {
	private static final long serialVersionUID = -5009616693873958408L;

	public CommunicationException(String msg, Throwable cause) {
		super(msg, cause, 500);
	}
}