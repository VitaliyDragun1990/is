package com.revenat.ishop.exception;

import com.revenat.ishop.exception.base.ApplicationException;

/**
 * This exception represents error when request resource can not be found.
 * @author Vitaly Dragun
 *
 */
public class ResourceNotFoundException extends ApplicationException {
	private static final long serialVersionUID = 8885825981842064855L;

	public ResourceNotFoundException(String message) {
		super(message, 404);
	}
}
