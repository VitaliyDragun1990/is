package com.revenat.ishop.infrastructure.framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Column;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Transient;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;

public final class ReflectionUtils {

	public static List<Field> getAccessibleFields(Class<?> entityClass) {
		List<Field> fields = new ArrayList<>();
		while (entityClass != Object.class) {
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

	public static Method findMethod(Method method, Class<?> classInstance) {
		for (Method m : classInstance.getDeclaredMethods()) {
			if (m.getName().equals(method.getName())
					&& Arrays.equals(m.getParameterTypes(), method.getParameterTypes())) {
				return m;
			}
		}
		throw new FrameworkSystemException("Can not ind method " + method + " in the " + classInstance);
	}

	/**
	 * Finds whether specified {@code method} is annotated with annotation with
	 * given {@code annotationClass} If methid has modifier different from {@code public} then
	 * annotation, whether it present or not, would be ignored.
	 * 
	 * @param method          method to check for annotation
	 * @param annotationClass class of the annotation to check
	 * @return annotation if method has one, or {@code null} otherwise
	 */
	public static <T extends Annotation> T findConfigAnnotaion(Method method, Class<T> annotationClass) {
		if (isMethodPublic(method)) {
			return findAnnotation(method, annotationClass);
		} else {
			return null;
		}
	}

	private static <T extends Annotation> T findAnnotation(Method method, Class<T> annotationClass) {
		T annotation = method.getAnnotation(annotationClass);
		if (annotation == null) {
			annotation = method.getDeclaringClass().getAnnotation(annotationClass);
		}
		return annotation;
	}

	private static boolean isMethodPublic(Method method) {
		return (method.getModifiers() & Modifier.PUBLIC) > 0;
	}

	private static boolean shouldFieldBeIncluded(Field field) {
		int modifiers = field.getModifiers();
		return (modifiers & (Modifier.STATIC | Modifier.FINAL)) == 0 && field.getAnnotation(Transient.class) == null;
	}

	private ReflectionUtils() {
	}
}
