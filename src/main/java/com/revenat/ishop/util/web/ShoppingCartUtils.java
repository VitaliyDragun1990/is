package com.revenat.ishop.util.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.config.Constants.Attribute;

/**
 * Utility class that contains convenient methods to facilitate work with
 * storing/retrieving/updating/deleting client's {@link ShoppingCart} instance.
 * 
 * @author Vitaly Dragun
 *
 */
@Deprecated
public class ShoppingCartUtils {

	/**
	 * Returns {@link ShoppingCart} instance associated with specified client's
	 * {@link HttpSession} object.
	 * 
	 * @param session client's {@link HttpSession} to look for {@link ShoppingCart}
	 *                in.
	 * @return {@link ShoppingCart} instance associated with specified client's
	 *         session. If specified {@link HttpSession} instance doesn't contain
	 *         shopping cart, new shopping cart will be created, placed inside
	 *         provided session and returned.
	 */
	public static ShoppingCart getCurrentShoppingCart(HttpSession session) {
		ShoppingCart cart = (ShoppingCart) session.getAttribute(Attribute.CURRENT_SHOPPING_CART);
		if (cart == null) {
			cart = new ShoppingCart();
			setCurrentShoppingCart(session, cart);
		}
		return cart;
	}

	/**
	 * Sets provided {@link ShoppingCart} instance into specified client's
	 * {@link HttpSession} object. If specified {@link HttpSession} already contains
	 * shopping cart, it will be replaced with provided one.
	 * 
	 * @param session      {@link HttpSession} instance to set shopping cart into.
	 * @param shoppingCart {@link ShoppingCart} instance to set into session object.
	 */
	public static void setCurrentShoppingCart(HttpSession session, ShoppingCart shoppingCart) {
		session.setAttribute(Attribute.CURRENT_SHOPPING_CART, shoppingCart);
	}

	/**
	 * Checks whether provided {@link HttpSession} instance contains
	 * {@link ShoppingCart} in it.
	 * 
	 * @param session {@link HttpSession} instance to check for shopping cart.
	 * @return {@code true} if given {@link HttpSession} contains client's
	 *         {@link ShoppingCart} in it, {@code false} otherwise.
	 */
	public static boolean isCurrentShoppingCartExists(HttpSession session) {
		return session.getAttribute(Attribute.CURRENT_SHOPPING_CART) != null;
	}

	/**
	 * Removes {@link ShoppingCart} instance, if any, from provided client's
	 * {@link HttpSession} object and set's special shopping cart {@link Cookie}
	 * into provided {@link HttpServletResponse} object in a way it makes client to
	 * delete given cookie from their side when they receive such response.
	 * 
	 * @param session {@link HttpSession} instance, associated with particular
	 *                client, to delete shopping cart from.
	 * @param resp    {@link HttpServletResponse} instance to particular client.
	 */
	public static void removeCurrentShoppingCart(HttpSession session, HttpServletResponse resp) {
		session.removeAttribute(Attribute.CURRENT_SHOPPING_CART);
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), null, 0, resp);
	}

	/**
	 * Search for special {@code 'shopping-cart'} {@link Cookie} object into
	 * provided client's {@link HttpServletRequest} and returns it if any.
	 * 
	 * @param req client's {@link HttpServletRequest} to search cookie in.
	 * @return {@code 'shopping-cart'} cookie if any, or {@code null} request
	 *         doesn't contain one.
	 */
	public static Cookie findShoppingCartCookie(HttpServletRequest req) {
		return WebUtils.findCookie(req, Constants.Cookie.SHOPPING_CART.getName());
	}

	/**
	 * Sets special {@code 'shopping-cart'} {@link Cookie} with specified value to
	 * provided {@link HttpServletResponse} object. If such response already
	 * contains given cookie, it's value will be updated.
	 * 
	 * @param cookieValue value of the {@code 'shopping-cart'} cookie to be set
	 *                    with.
	 * @param resp        {@link HttpServletResponse} to set cookie to.
	 */
	public static void setCurrentShoppingCartCookie(String cookieValue, HttpServletResponse resp) {
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), cookieValue,
				Constants.Cookie.SHOPPING_CART.getTtl(), resp);
	}

	private ShoppingCartUtils() {
	}
}
