package com.revenat.ishop.exception;

import com.revenat.ishop.exception.base.ApplicationException;

/**
 * Custom exception that represents validation error of user provided data.
 * 
 * @author Vitaly Dragun
 *
 */
public class ValidationException extends ApplicationException {
	private static final long serialVersionUID = 6176636234085711406L;

	public ValidationException(String message) {
		super(message, 400);
	}

}
