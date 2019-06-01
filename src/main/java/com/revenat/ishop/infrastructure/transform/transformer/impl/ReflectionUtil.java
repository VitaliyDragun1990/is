package com.revenat.ishop.infrastructure.transform.transformer.impl;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.revenat.ishop.infrastructure.transform.annotation.Ignore;
import com.revenat.ishop.infrastructure.transform.exception.TransformException;

/**
 * Contains reflection-related utility operations
 * 
 * @author Vitaly Dragun
 *
 */
final class ReflectionUtil {

	/**
	 * Creates new instance of the specified class.
	 * 
	 * @param clz class for instance to create
	 * @return new instance of the specified class
	 * @throws TransformException unchecked exception if creation fails for some
	 *                            reason
	 */
	static <T> T createInstance(Class<T> clz) throws TransformException {
		try {
			return clz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new TransformException(e);
		}
	}

	/**
	 * Returns list of field names for fields that have identical names in specified
	 * {@code clz1} and {@code clz2} class objects.
	 * 
	 * @throws TransformException unchecked exception if process fails for some
	 *                            reason
	 */
	static List<String> findSimilarFields(Class<?> clz1, Class<?> clz2) throws TransformException {
		try {
			List<String> clz1FieldNames = getFields(clz1).stream().filter(ReflectionUtil::isSupportedField)
					.map(Field::getName).collect(toList());

			List<String> clz2FieldNames = getFields(clz2).stream().filter(ReflectionUtil::isSupportedField)
					.map(Field::getName).collect(toList());

			return clz1FieldNames.stream().filter(clz2FieldNames::contains).collect(toList());
		} catch (SecurityException e) {
			throw new TransformException(e);
		}
	}

	/**
	 * Copies specified field values from the source object to the destination
	 * object.
	 * 
	 * @param source      Object to copy field values from
	 * @param destination Object to copy field values to
	 * @param fieldNames  List of field names to copy values from
	 * @throws TransformException unchecked exception if copying failed.
	 */
	static void copyFields(Object source, Object destination, List<String> fieldNames) throws TransformException {
		checkParam(source != null, "Source object is not initialized");
		checkParam(destination != null, "Destination object is not initialized");
		
		try {
			for (String fieldName : fieldNames) {
				Optional<Field> sourceFieldOptional = getField(source.getClass(), fieldName);
				// Skip unknown fields
				if (sourceFieldOptional.isPresent()) {
					Field sourceField = sourceFieldOptional.get();
					sourceField.setAccessible(true);
					Object valueToCopy = sourceField.get(source);
					
					Optional<Field> destFieldOptional = getField(destination.getClass(), fieldName);
					
					if (destFieldOptional.isPresent()) {
						Field destField = destFieldOptional.get();
						destField.setAccessible(true);
						destField.set(destination, valueToCopy);
					}
				}
			}
		} catch (SecurityException | ReflectiveOperationException | IllegalArgumentException e) {
			throw new TransformException(e);
		}
	}

	/**
	 * Returns class field by its name. This method supports base classes as well
	 * 
	 * @param clz       Class instance to look for the field in
	 * @param fieldName Name of the field to look for
	 * @return field instance with specified name if found, or {@code null} otherwise
	 */
	private static Optional<Field> getField(Class<?> clz, String fieldName) {
		Class<?> current = clz;
		while (current != null) {
			try {
				Field declaredField = current.getDeclaredField(fieldName);
				return Optional.of(declaredField);
			} catch (NoSuchFieldException | SecurityException e) {
				current = current.getSuperclass();
			}
		}
		return Optional.empty();
	}

	private static boolean isSupportedField(Field field) {
		return !field.isAnnotationPresent(Ignore.class) && !Modifier.isStatic(field.getModifiers())
				&& !Modifier.isFinal(field.getModifiers());
	}

	/**
	 * Returns all declared fields in the specified class and all its superclasses
	 */
	private static List<Field> getFields(Class<?> clz) {
		List<Field> fields = new ArrayList<>();
		while (clz != null) {
			fields.addAll(Arrays.asList(clz.getDeclaredFields()));
			clz = clz.getSuperclass();
		}

		return fields;
	}

	private static void checkParam(boolean condition, String msg) {
		if (!condition) {
			throw new TransformException(msg);
		}
	}

	private ReflectionUtil() {
	}
}
