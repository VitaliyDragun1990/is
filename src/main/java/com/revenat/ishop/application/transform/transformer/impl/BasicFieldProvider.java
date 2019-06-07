package com.revenat.ishop.application.transform.transformer.impl;

import java.util.List;

/**
 * Base functionality of the field preparation
 * 
 * @author Vitaly Dragun
 *
 */
public class BasicFieldProvider implements FieldProvider {

	@Override
	public List<String> getFieldNames(Class<?> source, Class<?> dest) {
		return ReflectionUtil.findSimilarFields(source, dest);
	}

}
