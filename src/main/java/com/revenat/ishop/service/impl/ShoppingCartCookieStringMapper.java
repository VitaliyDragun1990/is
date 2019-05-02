package com.revenat.ishop.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.entity.Product;
import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.repository.ProductRepository;
import com.revenat.ishop.service.ShoppingCartMapper;

/**
 * This component is responsible for marshalling {@link ShoppingCart} instance
 * into
 * 
 * responsible for mapping {@link ShoppingCart} instance to/from cookie string.
 * 
 * @author Vitaly Dragun
 *
 */
public class ShoppingCartCookieStringMapper implements ShoppingCartMapper<String> {
	private static final String CART_ITEM_CODING_FORMAT = "%d-%d|";
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartCookieStringMapper.class);
	
	private final ProductRepository productRepository;
	
	public ShoppingCartCookieStringMapper(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Marshals provided {@link ShoppingCart} instance into cookie string
	 * representation.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to marshal.
	 * @return cookie string representing {@link ShoppingCart} instance. Empty
	 *         string if specified {@link ShoppingCart} instance is empty.
	 * @throws NullPointerException if specified {@link ShoppingCart} instance is
	 *                              null;
	 */
	public String marshall(ShoppingCart shoppingCart) {
		StringBuilder cookieString = new StringBuilder();

		for (ShoppingCartItem item : shoppingCart.getItems()) {
			cookieString.append(String.format(CART_ITEM_CODING_FORMAT, item.getProduct().getId(), item.getQuantity()));
		}
		if (cookieString.length() > 0) {
			return cookieString.substring(0, cookieString.length() - 1);
		}

		return "";
	}

	/**
	 * Unmarshals given cookie string into {@link ShoppingCart} instance.
	 * 
	 * @param shoppingCartString cookie string representing {@link ShoppingCart}
	 * @return {@link ShoppingCart} instance obtained by unmarshalling cookie
	 *         string. If specified cookie string contains fragment with invalid
	 *         format, it will be ommited, if entire cookie string is in invalid
	 *         format then empty {@link ShoppingCart} instance will be returning
	 *         without throwing exception.
	 * @throws NullPointerException if specified string is {@code null}
	 */
	public ShoppingCart unmarshall(String shoppingCartString) {
		String[] tokens = shoppingCartString.split("\\|");
		return unmarshallShoppingCart(tokens);
	}

	private ShoppingCart unmarshallShoppingCart(String[] tokens) {
		ShoppingCart cart = new ShoppingCart();
		for (int i = 0; i < tokens.length; i++) {
			String cartItemString = tokens[i];
			addToCart(cart, cartItemString);
		}
		return cart;
	}

	private void addToCart(ShoppingCart cart, String cartItemString) {
		String[] cartItemTokens = cartItemString.split("-");
		int productId;
		int quantity;
		try {
			productId = Integer.parseInt(cartItemTokens[0]);
			quantity = Integer.parseInt(cartItemTokens[1]);
			Product p = loadProduct(productId);
			cart.addProduct(p, quantity);
		} catch (ValidationException e) {
			LOGGER.warn("{} Invalid cart item would be ommited.",e.getMessage());
		} catch (Exception e) {
			LOGGER.warn("shopping cart cookie fragment is invalid: {}. Valid format is 'productId-quantity'."
					+ " Invalid fragment would be ommited.",
					cartItemString);
		}
	}

	private Product loadProduct(int productId) {
		Product p = productRepository.getById(productId);
		if (p == null) {
			throw new ValidationException(
					"Error during unmarshalling shopping cart item: " + "product with id: " + productId + " not found.");
		}
		return p;
	}
}
