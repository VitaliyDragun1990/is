package com.revenat.ishop.repository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.exception.InternalServerException;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.service.application.ShoppingCartMapper;
import com.revenat.ishop.util.web.WebUtils;

public final class ShoppingCartRepository {
	private final ShoppingCartMapper<String> cartMapper;

	public ShoppingCartRepository(ShoppingCartMapper<String> cartMapper) {
		this.cartMapper = cartMapper;
	}

	/**
	 * Returns {@link ShoppingCart} instance associated with specified client's
	 * {@link HttpServletRequest} object. To be sure that this method won't throw
	 * {@link InternalServerException} {@link #loadShoppingCart(HttpServletRequest)}
	 * method should be called beforehed as necessary precondition.
	 * 
	 * @param clientSession client's session to look for {@link ShoppingCart}
	 *                      associated with.
	 * @return {@link ShoppingCart} instance associated with specified client's
	 *         request. If there is no {@link ShoppingCart} instance associated with
	 *         given request.
	 * @throws InternalServerException if there is no {@link ShoppingCart} object
	 *                               accosiated with given user's request.
	 */
	public ShoppingCart getShoppingCart(HttpSession clientSession) {
		return (ShoppingCart) clientSession.getAttribute(Attribute.CURRENT_SHOPPING_CART);
	}

	/**
	 * Sets provided {@link ShoppingCart} instance as attribute to provided users
	 * {@link HttpSession} object.
	 * 
	 */
	public void setShoppingCart(HttpSession userSession, ShoppingCart cart) {
		userSession.setAttribute(Attribute.CURRENT_SHOPPING_CART, cart);
	}

	/**
	 * This method is responsible for checking whether user's (that is represented
	 * by provided {@code request} object) session contains {@link ShoppingCart}
	 * object. If user's session already contains shopping cart object then this
	 * method does nothing, otherwise it checks for special cookie in user's request
	 * and if it finds such cookie, it tries to restore shopping cart from it. If
	 * shopping cart has been successfully restored from cookie it will be placed
	 * incide user's session. If restoration failed or if there was not such cookie
	 * in user's request at the first place, then new empty {@link ShoppingCart}
	 * instance will be created and placed inside user's session.
	 * 
	 * @param request client's request that represents particular user and is used
	 *                to get access to such user's session and special
	 *                'shopping-cart' cookie.
	 */
	public void loadShoppingCart(HttpServletRequest request) {
		ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute(Attribute.CURRENT_SHOPPING_CART);
		if (cart == null) {
			cart = getShoppingCartFromCookie(request);
			if (cart == null) {
				cart = new ShoppingCart();
			}
			request.getSession().setAttribute(Attribute.CURRENT_SHOPPING_CART, cart);
		}
	}

	/**
	 * Saves provided shopping cart state as cookie and place such cookie into
	 * specified {@link HttpServletResponse} object.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to persist as cookie.
	 */
	public void persistShoppingCartAsCookie(ShoppingCart shoppingCart, HttpServletResponse response) {
		saveShoppingCartAsCookie(shoppingCart, response);
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
	private ShoppingCart getShoppingCartFromCookie(HttpServletRequest request) {
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
	private void saveShoppingCartAsCookie(ShoppingCart shoppingCart, HttpServletResponse response) {
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
