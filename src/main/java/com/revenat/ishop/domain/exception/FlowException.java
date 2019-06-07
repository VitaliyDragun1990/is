package com.revenat.ishop.domain.exception;

import com.revenat.ishop.domain.exception.base.ApplicationException;

/**
 * Signals about exceptional cases in the application logic
 * 
 * @author Vitaly Dragun
 *
 */
public class FlowException extends ApplicationException {
	private static final long serialVersionUID = -1127720904105893710L;
	
	private final String messageCode;
	private final Object[] args;
	
	public FlowException(String message, Throwable cause, Object... args) {
		super(message, cause);
		messageCode = "message.error.500";
		this.args = args;
	}

	public FlowException(String message, String messageCode, Object... args) {
		super(message);
		this.messageCode = messageCode;
		this.args = args;
	}
	
	public FlowException(String message, Object... args) {
		super(message);
		messageCode = "message.error.500";
		this.args = args;
	}
	
	public String getMessageCode() {
		return messageCode;
	}
	
	public Object[] getArgs() {
		return args;
	}
}
