package com.revenat.ishop.ui.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.ui.config.Constants.Attribute;
import com.revenat.ishop.ui.service.ShoppingCartCookieSerializer;

/**
 * This filter responsible for deserializing customer's shopping cart from
 * special shopping-cart cookie if shopping cart is absent in customer's
 * {@link ClientSession} and aforementioned cookie is present and also for
 * serializing current shopping cart state as cookie.
 * 
 * @author Vitaly Dragun
 *
 */

public class ShoppingCartDeserializationFilter extends AbstractFilter {
	private static final String SHOPPING_CART_DESERIALIZATION_DONE = "SHOPPING_CART_DESERIALIZATION_DONE";

	private final ShoppingCartCookieSerializer cartSerializer;

	public ShoppingCartDeserializationFilter(ShoppingCartCookieSerializer cartSerializer) {
		this.cartSerializer = cartSerializer;
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!isCartDeserialized(request)) {
			processCartDeserialization(request);
		}

		chain.doFilter(request, response);

		processCartSerialization(request, response);
	}

	private boolean isCartDeserialized(HttpServletRequest request) {
		return request.getSession().getAttribute(SHOPPING_CART_DESERIALIZATION_DONE) != null;
	}

	private void processCartDeserialization(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		// get client session object
		ClientSession clientSession = getClientSession(httpSession);
		if (clientSession == null) {
			clientSession = deserializeCartFromCookie(request);
			httpSession.setAttribute(Attribute.CLIENT_SESSION, clientSession);
		}
		httpSession.setAttribute(SHOPPING_CART_DESERIALIZATION_DONE, Boolean.TRUE);
	}

	private ClientSession deserializeCartFromCookie(HttpServletRequest request) {
		ClientSession clientSession = new ClientSession();
		ShoppingCart cartFromCookie = cartSerializer.getShoppingCartFromCookie(request);
		if (cartFromCookie != null) {
			clientSession.setShoppingCart(cartFromCookie);
		}
		return clientSession;
	}

	private void processCartSerialization(HttpServletRequest request, HttpServletResponse response) {
		ClientSession clientSession = getClientSession(request.getSession());

		if (containsEmptyShoppingCart(clientSession)) {
			deleteShoppingCartCookie(request, response);
		} else {
			serializeShoppingCartAsCookie(clientSession, response);
		}
	}

	private boolean containsEmptyShoppingCart(ClientSession clientSession) {
		return clientSession != null && clientSession.getShoppingCart().isEmpty();
	}

	private void deleteShoppingCartCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie shoppingCartCookie = cartSerializer.findShoppingCartCookie(request);
		if (shoppingCartCookie != null) {
			cartSerializer.removeShoppingCartCookie(response);
		}
	}
	
	private void serializeShoppingCartAsCookie(ClientSession clientSession, HttpServletResponse response) {
		if (clientSession != null) {
			cartSerializer.saveShoppingCartAsCookie(clientSession.getShoppingCart(), response);
		}
	}

	private ClientSession getClientSession(HttpSession httpSession) {
		return (ClientSession) httpSession.getAttribute(Attribute.CLIENT_SESSION);
	}
}
