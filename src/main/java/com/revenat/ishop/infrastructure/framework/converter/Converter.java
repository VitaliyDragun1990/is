package com.revenat.ishop.infrastructure.framework.converter;

import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;

public interface Converter {

	/**
	 * Converts specified {@code value} parameter into class represented by
	 * {@code entityFieldClass} parameter if possible.
	 * 
	 * @param entityFieldClass class to which {@code value} should be converted to
	 * @param value            value to convert
	 * @return value converted to specified class
	 * @throws FrameworkSystemException if given value can not be converted to
	 *                                  specified class.
	 */
	<T> T convert(Class<T> entityFieldClass, Object value);
}
