package com.revenat.ishop.exception;

import com.revenat.ishop.exception.base.ApplicationException;

/**
 * This exception represents error in application's flow of execution. (e.g.
 * some method has been called without necessary preconditions had been met)
 * 
 * @author Vitaly Dragun
 *
 */
public class InternalServerException extends ApplicationException {
	private static final long serialVersionUID = -2501183003953472502L;

	public InternalServerException(String message) {
		super(message, 500);
	}
}
