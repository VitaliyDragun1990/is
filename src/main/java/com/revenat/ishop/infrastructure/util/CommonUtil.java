package com.revenat.ishop.infrastructure.util;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Contains utility functions of the general purpose
 * 
 * @author Vitaly Dragun
 *
 */
public final class CommonUtil {
	
	/**
	 * Returns not-null unmodifiable copy of the source set
	 */
	public static <T> Set<T> getSafeSet(Set<T> source) {
		return Collections.unmodifiableSet(
				Optional.ofNullable(source).orElse(Collections.emptySet())
				);
	}
	
	/**
	 * Returns non-null unmodifiable copy of the source list
	 */
	public static <T> List<T> getSafeList(List<T> source) {
		return Collections.unmodifiableList(
				Optional.ofNullable(source).orElse(Collections.emptyList())
				);
	}
	
	/**
	 * Generates string representation of the specified object
	 */
	public static String toString(Object param) {
		return ReflectionToStringBuilder.toString(param, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	private CommonUtil() {}
}
