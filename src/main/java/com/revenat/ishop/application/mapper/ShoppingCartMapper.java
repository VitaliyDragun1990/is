package com.revenat.ishop.application.mapper;

import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.domain.exception.flow.InvalidParameterException;

/**
 * This interface represents contract with responsibility of marshalling
 * {@link ShoppingCart} instance to <T> object and unmarshalling
 * {@link ShoppingCart} instance from <T> object.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ShoppingCartMapper<T> {

	/**
	 * Marshals provided {@link ShoppingCart} instance into T representation.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to marshal.
	 * @return T representation of the {@link ShoppingCart} instance.
	 * @throws InvalidParameterException if specified {@link ShoppingCart} instance is
	 *                              null;
	 */
	T marshall(ShoppingCart shoppingCart);

	/**
	 * Unmarshals given T value into {@link ShoppingCart} instance.
	 * 
	 * @param value T representation of the shopping cart
	 * @return {@link ShoppingCart} instance obtained by unmarshalling T
	 *         representation of the shopping cart content. If specified T value for
	 *         unmarshalling has invalid format, returns empty {@link ShoppingCart}
	 *         instance without throwing exception.
	 * @throws InvalidParameterException if specified T value is {@code null}
	 */
	ShoppingCart unmarshall(T value);
}
