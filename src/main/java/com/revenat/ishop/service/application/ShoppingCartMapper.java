package com.revenat.ishop.service.application;

import com.revenat.ishop.model.ShoppingCart;

/**
 * This interface represents contract with responsibility of marshalling
 * {@link ShoppingCart} instance to string object and unmarshalling
 * {@link ShoppingCart} instance from sting object.
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
	 * @throws NullPointerException if specified {@link ShoppingCart} instance is
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
	 * @throws NullPointerException if specified T value is {@code null}
	 */
	ShoppingCart unmarshall(T value);
}
