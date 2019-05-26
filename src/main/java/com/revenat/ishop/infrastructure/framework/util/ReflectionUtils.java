package com.revenat.ishop.infrastructure.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.revenat.ishop.infrastructure.framework.annotation.jdbc.Column;
import com.revenat.ishop.infrastructure.framework.annotation.jdbc.Transient;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;

public final class ReflectionUtils {
	
	public static List<Field> getAccessibleEntityFields(Class<?> entityClass) {
		List<Field> fields = new ArrayList<>();
		while(entityClass != Object.class) {
			for (Field field : entityClass.getDeclaredFields()) {
				if (shouldFieldBeIncluded(field)) {
					field.setAccessible(true);
					fields.add(field);
				}
			}
			entityClass = entityClass.getSuperclass();
		}
		
		return fields;
	}
	
	public static Field findField(Class<?> entityClass, List<Field> fields, String fieldName) {
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		throw new FrameworkSystemException("Field " + fieldName + " not found in class " + entityClass);
	}
	
	public static String getColumnNameForField(Field entityField) {
		Column columnAnnotation = entityField.getAnnotation(Column.class);
		if (columnAnnotation != null) {
			return columnAnnotation.value().toUpperCase();
		}
		return entityField.getName().toUpperCase();
	}

	private static boolean shouldFieldBeIncluded(Field field) {
		int modifiers = field.getModifiers();
		return (modifiers & (Modifier.STATIC | Modifier.FINAL)) == 0 && field.getAnnotation(Transient.class) == null;
	}

	private ReflectionUtils() {}
}
