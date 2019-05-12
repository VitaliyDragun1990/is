package com.revenat.ishop.application.infra.util;

import com.revenat.ishop.application.infra.exception.flow.InvalidParameterException;
import com.revenat.ishop.application.infra.exception.flow.ValidationException;

public class Checks {

	/**
	 * Verifies that specified check passed and throws exception otherwise
	 * 
	 * @param check   specific check to verify
	 * @param message specific message to pass to posiible exception
	 * @param args    optional arguments to message string
	 * @throws InvalidParameterException
	 */
	public static void checkParam(boolean check, String message, Object... args) throws InvalidParameterException {
		if (!check) {
			throw new InvalidParameterException(String.format(message, args));
		}
	}

	/**
	 * Verifies that specified condition not reached and throws exception otherwise
	 * 
	 * @param condition specific condition to verify
	 * @param message   specific message to pass to posiible exception
	 * @param args      optional arguments to message string
	 * @throws ValidationException
	 */
	public static void validateCondition(boolean condition, String message, Object... args) throws ValidationException {
		if (condition) {
			throw new ValidationException(String.format(message, args));
		}
	}

	private Checks() {
	}
}
