package com.revenat.ishop.util;

import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ShoppingCart;

/**
 * This interface represents component responsible for unmarshalling
 * {@link ShoppingCart} instance from it's string representation.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ShoppingCartUnmarshaller {

	/**
	 * Unmarshals given string into {@link ShoppingCart} instance.
	 * 
	 * @param shoppingCartString string representation of the shopping cart
	 * @return {@link ShoppingCart} instance obtained by unmarshalling string
	 *         representation of the shopping cart content.
	 * @throws NullPointerException if specified string is {@code null}
	 * @throws ValidationException if specified string is in invalid format
	 * and cannot be unmarshalled correctly.
	 */
	ShoppingCart fromString(String shoppingCartString);
}
