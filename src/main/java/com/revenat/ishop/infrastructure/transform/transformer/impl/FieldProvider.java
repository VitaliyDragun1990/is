package com.revenat.ishop.infrastructure.transform.transformer.impl;

import java.util.List;

/**
 * Functionality of field preparation
 * 
 * @author Vitaly Dragun
 *
 */
public interface FieldProvider {

	/**
	 * Returns list of similar field names for source/destination classes
	 */
	List<String> getFieldNames(Class<?> source, Class<?> dest);
}
