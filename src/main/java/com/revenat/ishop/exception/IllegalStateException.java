package com.revenat.ishop.exception;

/**
 * This exception represents error in application's flow of execution. (e.g.
 * some method has been called without necessary preconditions had been met)
 * 
 * @author Vitaly Dragun
 *
 */
public class IllegalStateException extends ApplicationException {
	private static final long serialVersionUID = -2501183003953472502L;

	public IllegalStateException(String message) {
		super(message);
	}
}
