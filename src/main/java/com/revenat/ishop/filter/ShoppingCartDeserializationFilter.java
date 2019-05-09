package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.model.ClientSession;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.service.ServiceManager;
import com.revenat.ishop.service.application.ShoppingCartCookieSerializer;

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

	private ShoppingCartCookieSerializer cartSerializer;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServiceManager serviceManager = (ServiceManager) filterConfig.getServletContext()
				.getAttribute(Attribute.SERVICE_MANAGER);
		this.cartSerializer = serviceManager.getCartSerializer();
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (!cartDeserialized(request)) {
			processCartDeserialization(request);
		}

		chain.doFilter(request, response);

		processCartSerialization(request, response);
	}

	private boolean cartDeserialized(HttpServletRequest request) {
		return request.getSession().getAttribute(SHOPPING_CART_DESERIALIZATION_DONE) != null;
	}
	
	private void processCartDeserialization(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		// get client session object
		ClientSession clientSession = getClientSession(httpSession);
		if (clientSession == null) {
			// if it is null - create new one
			clientSession = new ClientSession();
			// try to serialize shopping cart from cookie
			ShoppingCart cartFromCookie = cartSerializer.getShoppingCartFromCookie(request);
			if (cartFromCookie != null) {
				clientSession.setShoppingCart(cartFromCookie);
			}
			httpSession.setAttribute(Attribute.CLIENT_SESSION, clientSession);
		}
		httpSession.setAttribute(SHOPPING_CART_DESERIALIZATION_DONE, Boolean.TRUE);
	}

	private void processCartSerialization(HttpServletRequest request, HttpServletResponse response) {
		// check whether client session is present and shopping cart is empty
		ClientSession clientSession = getClientSession(request.getSession());
		if (clientSession != null && clientSession.getShoppingCart().isEmpty()) {
			// if true check if request contained shopping-cart cookie
			Cookie shoppingCartCookie = cartSerializer.findShoppingCartCookie(request);
			if (shoppingCartCookie != null) {
				// if true set shopping cart cookie to remove via response
				cartSerializer.removeShoppingCartCookie(response);
			}
		}
		// else save current shopping cart content as cookie
		else if (clientSession != null) {
			cartSerializer.saveShoppingCartAsCookie(clientSession.getShoppingCart(), response);
		}
	}

	private ClientSession getClientSession(HttpSession httpSession) {
		return (ClientSession) httpSession.getAttribute(Attribute.CLIENT_SESSION);
	}
}
