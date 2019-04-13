package com.revenat.ishop.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.model.ShoppingCart;

public class SessionUtils {
	
	public static ShoppingCart getCurrentShoppingCart(HttpServletRequest req) {
		ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART);
		if (cart == null) {
			cart = new ShoppingCart();
			setCurrentShoppingCart(req, cart);
		}
		return cart;
	}
	
	public static void setCurrentShoppingCart(HttpServletRequest req, ShoppingCart shoppingCart) {
		req.getSession().setAttribute(Constants.CURRENT_SHOPPING_CART, shoppingCart);
	}
	
	public static boolean isCurrentShoppingCartCreated(HttpServletRequest req) {
		return req.getSession().getAttribute(Constants.CURRENT_SHOPPING_CART) != null;
	}
	
	public static void clearCurrentShoppingCart(HttpServletRequest req, HttpServletResponse resp) {
		req.getSession().removeAttribute(Constants.CURRENT_SHOPPING_CART);
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), null, 0, resp);
	}
	
	public static Cookie findShoppingCartCookie(HttpServletRequest req) {
		return WebUtils.findCookie(req, Constants.Cookie.SHOPPING_CART.getName());
	}
	
	public static void updateCurrentShoppingCartCookie(String cookieValue, HttpServletResponse resp) {
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), cookieValue,
				Constants.Cookie.SHOPPING_CART.getTtl(), resp);
	}

	private SessionUtils() { 
	}
}
