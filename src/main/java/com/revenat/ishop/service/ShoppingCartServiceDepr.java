package com.revenat.ishop.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.service.impl.ShoppingCartCookieStringMapper;
import com.revenat.ishop.util.web.WebUtils;
import com.revenat.ishop.config.Constants.Attribute;

/**
 * This component incapsulates CRUD shopping cart logic. It contains various
 * methods for retrieving, updating, setting, deleting clien't
 * {@link ShoppingCart} object mainly via client's {@link HttpSession} or
 * special {@link Cookie}.
 * 
 * @author Vitaly Dragun
 *
 */
@Deprecated
public class ShoppingCartServiceDepr {
	private final ShoppingCartCookieStringMapper cartMapper;

	public ShoppingCartServiceDepr(ShoppingCartCookieStringMapper cartMapper) {
		this.cartMapper = cartMapper;
	}

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
	public ShoppingCart getShoppingCart(HttpSession session) {
		ShoppingCart cart = (ShoppingCart) session.getAttribute(Attribute.CURRENT_SHOPPING_CART);
		if (cart == null) {
			cart = new ShoppingCart();
			setShoppingCart(session, cart);
		}
		return cart;
	}

	/**
	 * Associates provided {@link ShoppingCart} instance with specified client's
	 * {@link HttpSession} instance. If specified {@link HttpSession} already
	 * contains shopping cart, it will be replaced with provided one.
	 * 
	 * @param session      {@link HttpSession} instance to set shopping cart into.
	 * @param shoppingCart {@link ShoppingCart} instance to set into session object.
	 */
	public void setShoppingCart(HttpSession session, ShoppingCart shoppingCart) {
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
	public boolean isShoppingCartExists(HttpSession session) {
		return session.getAttribute(Attribute.CURRENT_SHOPPING_CART) != null;
	}

	/**
	 * Removes {@link ShoppingCart} instance, if any, from provided client's
	 * {@link HttpSession} instance and sets special shopping cart {@link Cookie}
	 * into provided {@link HttpServletResponse} object in a way it makes client to
	 * delete given cookie from their side when they receive such response.
	 * 
	 * @param session  {@link HttpSession} instance, associated with particular
	 *                 client, to delete shopping cart from.
	 * @param response {@link HttpServletResponse} instance to particular client.
	 */
	public void removeShoppingCart(HttpSession session, HttpServletResponse response) {
		session.removeAttribute(Attribute.CURRENT_SHOPPING_CART);
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), null, 0, response);
	}

	/**
	 * Retrieves client's {@link ShoppingCart} using special cookie from provided
	 * {@link HttpServletRequest}.
	 * 
	 * @param request {@link HttpServletRequest} object that represents clients
	 *                request that may contain shopping cart cookie.
	 * @return client's {@link ShoppingCart} created from cookie, or {@code null} if
	 *         client's request does not contain such cookie.
	 */
	public ShoppingCart getShoppingCartFromCookie(HttpServletRequest request) {
		Cookie shoppingCartCookie = findShoppingCartCookie(request);
		if (shoppingCartCookie != null) {
			return cartMapper.unmarshall(shoppingCartCookie.getValue());
		}
		return null;
	}

	/**
	 * Saves specified {@link ShoppingCart} instance in form of cookie into provided
	 * {@link HttpServletResponse} objects wich represents response to the client.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to save as cookie.
	 * @param response     {@link HttpServletResponse} that represents response to
	 *                     the client with command to set such cookie for subsequent
	 *                     client's requests.
	 */
	public void saveShoppingCartAsCookie(ShoppingCart shoppingCart, HttpServletResponse response) {
		String shoppingCartCookie = cartMapper.marshall(shoppingCart);
		setShoppingCartCookie(shoppingCartCookie, response);
	}

	/**
	 * Removes special 'shopping cart' cookie by setting it time-to-live value to
	 * {@code 0} in the response to client.
	 * 
	 * @param response {@link HttpServletResponse} that represents response to the
	 *                 client.
	 */
	public void removeShoppingCartCookie(HttpServletResponse response) {
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), null, 0, response);
	}

	/**
	 * Search for special {@code 'shopping-cart'} {@link Cookie} object into
	 * provided client's {@link HttpServletRequest} and returns it if any.
	 * 
	 * @param req client's {@link HttpServletRequest} to search cookie in.
	 * @return {@code 'shopping-cart'} cookie if any, or {@code null} request
	 *         doesn't contain one.
	 */
	private Cookie findShoppingCartCookie(HttpServletRequest req) {
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
	private void setShoppingCartCookie(String cookieValue, HttpServletResponse resp) {
		WebUtils.setCookie(Constants.Cookie.SHOPPING_CART.getName(), cookieValue,
				Constants.Cookie.SHOPPING_CART.getTtl(), resp);
	}
}
