package com.revenat.ishop.infrastructure.exception.flow;

/**
 * Raised when attribute values of the object model violates business rules or
 * restrictions
 * 
 * @author Vitaly Dragun
 *
 */
public class ValidationException extends FlowException {
	private static final long serialVersionUID = 6176636234085711406L;

	public ValidationException(String message) {
		super(message, 400);
	}
}
