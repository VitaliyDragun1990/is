package com.revenat.ishop.application.transform.transformer;

import java.util.List;

import com.revenat.ishop.application.transform.Transformable;

/**
 * Represents transformation engine for converting business entities into DTO
 * objects
 * 
 * @author Vitaly Dragun
 *
 * @param <T> business entity type
 * @param <P> DTO type that can be converted to/from business entity type
 */
public interface Transformer {

	/**
	 * Converts specified entity into new DTO object
	 * 
	 * @param entity   entity instance to convert from
	 * @param dtoClass Class of the specified DTO object to convert to
	 * @return instance of the specified DTO
	 */
	<T, P extends Transformable<T>> P transform(T entity, Class<P> dtoClass);
	
	/**
	 * Converts specified entities into new DTO objects
	 * @param entities List of entities to convert from
	 * @param dtoClass Class of the specified DTO object to convert to
	 * @return List of the specified DTOs
	 */
	<T, P extends Transformable<T>> List<P> transfrom(Iterable<T> entities, Class<P> dtoClass);

	/**
	 * Converts specified entity into existing DTO object
	 * 
	 * @param entity specified entity to convert from
	 * @param dto    specified DTO to convert to
	 */
	<T, P extends Transformable<T>> void transform(T entity, P dto);
	
	/**
	 * Converts specified DTO object into business entity
	 * 
	 * @param dto DTO instance to convert from
	 * @param entityClass Class of the specified entity to convert to
	 * @return instance of the specified entity
	 */
	<T, P extends Transformable<T>> T untransform(P dto, Class<T> entityClass);
	
	/**
	 * Converts specified dto objects into entity objects
	 * @param dtos List Of DTO objects to convert from
	 * @param entityClass Class of the specified entity object to convert to
	 * @return List of the specified entity objects
	 */
	<T, P extends Transformable<T>> List<T> untransfrom(List<P> dtos, Class<T> entityClass);
}
