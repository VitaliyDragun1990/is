package com.revenat.ishop.application.infra.exception.flow;

/**
 * Signals thet incorrect parameter was passed to method/constructor.
 * 
 * @author Vitaly Dragun
 *
 */
public class InvalidParameterException extends FlowException {
	private static final long serialVersionUID = 5267570694141696972L;

	public InvalidParameterException(String message) {
		super(message, 400);
	}
}
