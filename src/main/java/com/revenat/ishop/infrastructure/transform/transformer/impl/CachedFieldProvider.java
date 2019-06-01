package com.revenat.ishop.infrastructure.transform.transformer.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revenat.ishop.infrastructure.framework.annotation.di.Component;

@Component
public class CachedFieldProvider implements FieldProvider {
	
	/**
	 * Mapping between transformation pair(concatenated class names) and field list
	 */
	private final Map<String, List<String>> cache;
	
	public CachedFieldProvider() {
		this.cache = new HashMap<>();
	}

	@Override
	public List<String> getFieldNames(Class<?> source, Class<?> dest) {
		String key = source.getSimpleName() + dest.getSimpleName();
		return cache.computeIfAbsent(key, k -> ReflectionUtil.findSimilarFields(source, dest));
	}

}
