package com.revenat.ishop.infrastructure.framework.converter.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.revenat.ishop.infrastructure.framework.converter.Converter;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;

public class DefaultConverter implements Converter {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T convert(Class<T> entityFieldClass, Object value) {
		if (value == null) {
			return null;
		} else if(entityFieldClass == Object.class || entityFieldClass == value.getClass()) {
			return entityFieldClass.cast(value);
		} else if (entityFieldClass == String.class) {
			return entityFieldClass.cast(value.toString());
		} else if (entityFieldClass == LocalDateTime.class && value.getClass() == Timestamp.class) {
			Timestamp fromDb = (Timestamp) value;
			return (T) fromDb.toLocalDateTime();
		} else {
			throw new FrameworkSystemException("Can not convert class " + value.getClass() + " to class " + entityFieldClass);
		}
	}

}
