package com.revenat.ishop.application.transform;

import com.revenat.ishop.application.transform.transformer.Transformer;

/**
 * Any object that support direct/backward transformation into some kind of
 * other object
 * 
 * @author Vitaly Dragun
 *
 */
public interface Transformable<T> {

	/**
	 * Transforms given object into current one
	 * 
	 * @param t object to transform from
	 */
	void transform(T t, Transformer tarnsformer);

	/**
	 * Transform current object into given one
	 * 
	 * @param t object to transform to
	 * @return transformet object
	 */
	T untransform(T t, Transformer tarnsformer);
}
