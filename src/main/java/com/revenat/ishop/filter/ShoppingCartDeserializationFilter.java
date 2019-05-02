package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.repository.ShoppingCartRepository;
import com.revenat.ishop.service.impl.ServiceManager;

/**
 * This filter responsible for deserializing customer's shopping cart from
 * special shopping-cart cookie if shopping cart is absent in customer's
 * {@link HttpSession} and aforementioned cookie is present.
 * 
 * @author Vitaly Dragun
 *
 */

public class ShoppingCartDeserializationFilter extends AbstractFilter {
	private static final String SHOPPING_CART_DESERIALIZATION_DONE = "SHOPPING_CART_DESERIALIZATION_DONE";

	private ShoppingCartRepository shoppingCartRepository;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServiceManager serviceManager = (ServiceManager) filterConfig.getServletContext()
				.getAttribute(Attribute.SERVICE_MANAGER);
		this.shoppingCartRepository = serviceManager.getShoppingCartRepository();
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = request.getSession();

		if (session.getAttribute(SHOPPING_CART_DESERIALIZATION_DONE) == null) {
			shoppingCartRepository.loadShoppingCart(request);
			session.setAttribute(SHOPPING_CART_DESERIALIZATION_DONE, Boolean.TRUE);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
