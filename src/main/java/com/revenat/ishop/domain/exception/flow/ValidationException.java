package com.revenat.ishop.domain.exception.flow;

import com.revenat.ishop.domain.exception.FlowException;

/**
 * Raised when attribute values of the object model violates business rules or
 * restrictions
 * 
 * @author Vitaly Dragun
 *
 */
public class ValidationException extends FlowException {
	private static final long serialVersionUID = 6176636234085711406L;

	public ValidationException(String message, String messageCode, Object... args) {
		super(message, messageCode, args);
	}
}
