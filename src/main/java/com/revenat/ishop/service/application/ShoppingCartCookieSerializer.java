package com.revenat.ishop.service.application;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.util.web.WebUtils;

/**
 * This component is responsible for serializing {@link ShoppingCart} object
 * to/from cookie.
 * 
 * @author Vitaly Dragun
 *
 */
public class ShoppingCartCookieSerializer {
	private final ShoppingCartMapper<String> cartMapper;

	public ShoppingCartCookieSerializer(ShoppingCartMapper<String> cartMapper) {
		this.cartMapper = cartMapper;
	}

	/**
	 * Sets special 'shopping cart' cookie into provided {@link HttpServletResponse}
	 * object in a way it makes client to delete given cookie from their side when
	 * they receive such response.
	 * 
	 * @param response {@link HttpServletResponse} instance associated with
	 *                 particular client to set 'shopping-cart' cookie to.
	 */
	public void removeShoppingCartCookie(HttpServletResponse response) {
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
	 * Search for special {@code 'shopping-cart'} {@link Cookie} object into
	 * provided client's {@link HttpServletRequest} and returns it if any.
	 * 
	 * @param req client's {@link HttpServletRequest} to search cookie in.
	 * @return {@code 'shopping-cart'} cookie if any, or {@code null} request
	 *         doesn't contain one.
	 */
	public Cookie findShoppingCartCookie(HttpServletRequest req) {
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
