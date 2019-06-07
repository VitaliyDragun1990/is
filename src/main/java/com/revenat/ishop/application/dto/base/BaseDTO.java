package com.revenat.ishop.application.dto.base;

import java.io.Serializable;

import com.revenat.ishop.application.transform.Transformable;
import com.revenat.ishop.application.transform.transformer.Transformer;
import com.revenat.ishop.application.util.CommonUtil;
import com.revenat.ishop.domain.entity.AbstractEntity;

/**
 * Base class for all DTO classes
 * 
 * @author Vitaly Dragun
 *
 */
public abstract class BaseDTO<K extends Serializable, T extends AbstractEntity<K>> implements Transformable<T> {

	/**
	 * Unique entity identifier
	 */
	private K id;

	public K getId() {
		return id;
	}
	
	/**
	 * Should be overridden in the derived classes if the additional transformation
	 * logic domain model -> DTO is needed. Overridden methods should call
	 * super.transform()
	 */
	@Override
	public void transform(T entity, Transformer transformer) {
		this.id = entity.getId();
	}
	
	/**
	 * Should be overridden in the derived classes if the additional transformation
	 * logic DTO -> domain model is needed.
	 */
	@Override
	public T untransform(T entity, Transformer transformer) {
		entity.setId(id);
		return entity;
	}

	public void setId(K id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return CommonUtil.toString(this);
	}
}
