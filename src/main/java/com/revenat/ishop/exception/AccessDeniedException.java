package com.revenat.ishop.exception;

/**
 * This exception represents situation when user does not have required
 * permissions to get requested resource.
 * 
 * @author Vitaly Dragun
 *
 */
public class AccessDeniedException extends ApplicationException {
	private static final long serialVersionUID = -3739773835132414549L;

	public AccessDeniedException(String message) {
		super(message);
	}
}
