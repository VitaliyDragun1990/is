package com.revenat.ishop.infrastructure.exception.flow;

/**
 * Signals thet incorrect parameter was passed to method/constructor.
 * 
 * @author Vitaly Dragun
 *
 */
public class InvalidParameterException extends FlowException {
	private static final long serialVersionUID = 5267570694141696972L;

	public InvalidParameterException(String message, String messageCode, Object... args) {
		super(message, messageCode, args);
	}
}
