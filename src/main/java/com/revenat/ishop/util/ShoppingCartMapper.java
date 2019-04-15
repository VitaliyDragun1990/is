package com.revenat.ishop.util;

import com.revenat.ishop.model.ShoppingCart;

/**
 * This component responsible for marshalling {@link ShoppingCart} instance
 * to/from string representation.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ShoppingCartMapper {

	/**
	 * Marshals provided {@link ShoppingCart} instance into string representation.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to marshal.
	 * @return string representation of the {@link ShoppingCart} instance.
	 * @throws NullPointerException if specified {@link ShoppingCart} instance is
	 *                              null;
	 */
	String toString(ShoppingCart shoppingCart);

	/**
	 * Unmarshals given string into {@link ShoppingCart} instance.
	 * 
	 * @param shoppingCartString string representation of the shopping cart
	 * @return {@link ShoppingCart} instance obtained by unmarshalling string
	 *         representation of the shopping cart content. If specified string for
	 *         unmarshalling has invalid format, returns empty {@link ShoppingCart}
	 *         instance without throwing exception.
	 * @throws NullPointerException if specified string is {@code null}
	 */
	ShoppingCart fromString(String shoppingCartString);
}
