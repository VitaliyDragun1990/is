package com.revenat.ishop.infrastructure.transform.transformer.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revenat.ishop.infrastructure.transform.Transformable;
import com.revenat.ishop.infrastructure.transform.exception.TransformException;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

/**
 * Default transformation engine that uses reflection to transform objects
 * 
 * @author Vitaly Dragun
 *
 */
@Component
public class SimpleDTOTransformer implements Transformer {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDTOTransformer.class);
	
	private final FieldProvider fieldProvider;
	
	
	@Autowired
	public SimpleDTOTransformer(FieldProvider fieldProvider) {
		this.fieldProvider = fieldProvider;
	}

	@Override
	public <T, P extends Transformable<T>> P transform(final T entity, final Class<P> dtoClass) {
		checkParams(entity, dtoClass);
		
		return handleTransformation(entity, ReflectionUtil.createInstance(dtoClass));
	}
	
	@Override
	public <T, P extends Transformable<T>> List<P> transfrom(Iterable<T> entities, Class<P> dtoClass) {
		checkParams(entities, dtoClass);
		
		List<P> list = new ArrayList<>();
		for (T entity : entities) {
			P dto = transform(entity, dtoClass);
			list.add(dto);
		}
		return list;
	}

	@Override
	public <T, P extends Transformable<T>> void transform(T entity, P dto) {
		checkParam(entity != null, "Source transformation object is not initialized");
		checkParam(dto != null, "Destination transformation object is not initialized");
		
		handleTransformation(entity, dto);
	}

	private <T, P extends Transformable<T>> P handleTransformation(final T entity, final P dto) {
		// Copy all the similar fields
		ReflectionUtil.copyFields(entity, dto, fieldProvider.getFieldNames(entity.getClass(), dto.getClass()));
		// Some custom transformation logic
		dto.transform(entity, this);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SimpleDTOTransformer.transform: {} DTO object", toString(dto));
		}
		
		return dto;
	}

	@Override
	public <T, P extends Transformable<T>> T untransform(P dto, Class<T> entityClass) {
		checkParams(dto, entityClass);
		
		T entity = ReflectionUtil.createInstance(entityClass);
		ReflectionUtil.copyFields(dto, entity, fieldProvider.getFieldNames(dto.getClass(), entityClass));
		// Some custom transformation logic
		dto.untransform(entity, this);
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SimpleDTOTransformer.untransform: {} entityObject", toString(entity));
		}
		
		return entity;
	}
	
	@Override
	public <T, P extends Transformable<T>> List<T> untransfrom(List<P> dtos, Class<T> entityClass) {
		checkParams(dtos, entityClass);
		
		List<T> list = new ArrayList<>();
		for (P dto : dtos) {
			T entity = untransform(dto, entityClass);
			list.add(entity);
		}
		return list;
	}
	
	private static String toString(Object obj) {
		return ReflectionToStringBuilder.toString(obj, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	private static void checkParams(Object source, Class<?> destClass) {
		checkParam(source != null, "Source transformation object is not initialized");
		checkParam(destClass != null, "No target class is defined for transformation");
	}
	
	private static void checkParam(boolean condition, String msg) {
		if (!condition) {
			throw new TransformException(msg);
		}
	}
}
