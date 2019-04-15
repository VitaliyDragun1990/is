package com.revenat.ishop.util;

import com.revenat.ishop.model.ShoppingCart;

/**
 * This interface represents component responsible for marshalling
 * {@link ShoppingCart} instance into string representation.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ShoppingCartMarshaller {

	/**
	 * Marshals provided {@link ShoppingCart} instance into string representation.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to marshal.
	 * @return string representation of the {@link ShoppingCart} instance.
	 * @throws NullPointerException if specified {@link ShoppingCart} instance is null;
	 */
	String toString(ShoppingCart shoppingCart);
}
