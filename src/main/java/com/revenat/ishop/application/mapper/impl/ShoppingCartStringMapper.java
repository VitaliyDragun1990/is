package com.revenat.ishop.application.mapper.impl;

import static java.util.Objects.requireNonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revenat.ishop.application.exception.ResourceNotFoundException;
import com.revenat.ishop.application.mapper.ShoppingCartMapper;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.application.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.domain.exception.flow.InvalidParameterException;
import com.revenat.ishop.domain.exception.flow.ValidationException;


/**
 * This component is responsible for mapping {@link ShoppingCart} instance
 * to/from string.
 * 
 * @author Vitaly Dragun
 *
 */
@Service
public class ShoppingCartStringMapper implements ShoppingCartMapper<String> {
	private static final String CART_ITEM_CODING_FORMAT = "%d-%d|";
	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartStringMapper.class);

	private ShoppingCartService cartService;
	
	@Autowired
	public ShoppingCartStringMapper(ShoppingCartService cartService) {
		this.cartService = cartService;
	}

	/**
	 * Marshals provided {@link ShoppingCart} instance into string
	 * representation.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to marshal.
	 * @return string representing {@link ShoppingCart} instance. Empty
	 *         string if specified {@link ShoppingCart} instance is empty.
	 * @throws InvalidParameterException if specified {@link ShoppingCart} instance is
	 *                              null;
	 */
	@Override
	public String marshall(ShoppingCart shoppingCart) {
		checkParam(shoppingCart);
		
		return mapCartToString(shoppingCart);
	}

	private String mapCartToString(ShoppingCart shoppingCart) {
		StringBuilder cartString = new StringBuilder();

		for (ShoppingCartItem item : shoppingCart.getItems()) {
			cartString.append(String.format(CART_ITEM_CODING_FORMAT, item.getProduct().getId(), item.getQuantity()));
		}
		
		if (cartString.length() > 0) {
			return cartString.substring(0, cartString.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * Unmarshals given {@code shoppingCartString} into {@link ShoppingCart}
	 * instance.
	 * 
	 * @param shoppingCartString string representing {@link ShoppingCart}. If
	 *                           specified string contains fragment with invalid
	 *                           format, it will be ommited, if entire string has
	 *                           invalid format then empty {@link ShoppingCart}
	 *                           instance will be returning without throwing
	 *                           exception.
	 * @return {@link ShoppingCart} instance obtained by unmarshalling string.
	 * @throws InvalidParameterException if specified string is {@code null}
	 */
	@Override
	public ShoppingCart unmarshall(String shoppingCartString) {
		checkParam(shoppingCartString);
		
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
			cartService.addProductToShoppingCart(productId, quantity, cart);
		} catch (ResourceNotFoundException | ValidationException e) {
			LOGGER.warn("{} Incorrect cart item will be ommited.", e.getMessage());
		} catch (Exception e) {
			LOGGER.warn("shopping cart string fragment is invalid: {}. Valid format is 'productId-quantity'."
					+ " Invalid fragment will be ommited.", cartItemString);
		}
	}
	
	private static void checkParam(ShoppingCart shoppingCart) {
		requireNonNull(shoppingCart != null, "Shopping cart to marshal can not be null");
	}
	
	private static void checkParam(String shoppingCartString) {
		requireNonNull(shoppingCartString != null, "String to unmarshall shopping cart from cookie not be null");
		
	}
}
