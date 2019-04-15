package com.revenat.ishop.util;

import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;

/**
 * This is default implementation of the {@link ShoppingCartMapper} which is
 * responsible for mapping {@link ShoppingCart} instance to/from cookie string.
 * 
 * @author Vitaly Dragun
 *
 */
public class ShoppingCartCookieMapper implements ShoppingCartMapper {

	@Override
	public String toString(ShoppingCart shoppingCart) {
		String cookieString = "";

		for (ShoppingCartItem item : shoppingCart.getItems()) {
			cookieString += String.format("%d-%d|", item.getProductId(), item.getQuantity());
		}
		if (cookieString.length() > 0) {
			cookieString = cookieString.substring(0, cookieString.length()-1);
		}

		return cookieString;
	}

	@Override
	public ShoppingCart fromString(String shoppingCartString) {
		String[] tokens = shoppingCartString.split("\\|");
		return unmarshallShoppingCart(tokens);
	}

	private ShoppingCart unmarshallShoppingCart(String[] tokens) {
		ShoppingCart cart = new ShoppingCart();
		for (int i = 0; i < tokens.length; i++) {
			String cartItemString = tokens[i];
			populateShoppingCart(cart, cartItemString);
		}
		return cart;
	}

	private void populateShoppingCart(ShoppingCart cart, String cartItemString) {
		String[] cartItemTokens = cartItemString.split("-");
		int productId;
		int quantity;
		try {
			productId = Integer.parseInt(cartItemTokens[0]);
			quantity = Integer.parseInt(cartItemTokens[1]);
			cart.addProduct(productId, quantity);
		} catch (Exception e) {
			// TODO: Log invalid cart item section
//			System.err.println("productToQuantity cookie string segment is invalid: " + cartItemString);
		}
	}

}
