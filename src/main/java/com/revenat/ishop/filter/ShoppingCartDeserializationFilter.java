package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.util.ShoppingCartMapper;
import com.revenat.ishop.util.ShoppingCartUtils;

/**
 * This filter responsible for deserializing customer's shopping cart from
 * special shopping-cart cookie if shopping cart is absent in customer's
 * {@link HttpSession} and aforementioned cookie is present.
 * 
 * @author Vitaly Dragun
 *
 */
public class ShoppingCartDeserializationFilter implements Filter {
	private static final String SHOPPING_CART_DESERIALIZATION_DONE = "SHOPPING_CART_DESERIALIZATION_DONE";

	private final ShoppingCartMapper cookieToCartMapper;

	public ShoppingCartDeserializationFilter(ShoppingCartMapper cookieMapper) {
		this.cookieToCartMapper = cookieMapper;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();

		if (session.getAttribute(SHOPPING_CART_DESERIALIZATION_DONE) == null
				&& !ShoppingCartUtils.isCurrentShoppingCartExists(session)) {
			
			Cookie cartCookie = ShoppingCartUtils.findShoppingCartCookie(req);
			if (cartCookie != null) {
				ShoppingCart cart = cookieToCartMapper.fromString(cartCookie.getValue());
				ShoppingCartUtils.setCurrentShoppingCart(session, cart);
			}
			session.setAttribute(SHOPPING_CART_DESERIALIZATION_DONE, Boolean.TRUE);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
