package com.revenat.ishop.presentation.infra.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Helper utility class that contains methods to facilitate work with
 * web-related tasks.
 * 
 * @author Vitaly Dragun
 *
 */
public class WebUtils {

	/**
	 * Finds {@link Cookie} with specified {@code cookieName} in the provided
	 * {@link HttpServletRequest} object.
	 * 
	 * @param req        {@link HttpServletRequest} object to find cookie in.
	 * @param cookieName name of the cookie to find.
	 * @return cookie with specified name if the search was successful, or
	 *         {@code null} if there is no cookie with such name, or if such cookie
	 *         exists but contains {@code null} or effectively empty value.
	 */
	public static Cookie findCookie(HttpServletRequest req, String cookieName) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName) && isExist(cookie.getValue())) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * Sets {@link Cookie} with specified name, value and age parameters to the
	 * provided {@link HttpServletResponse} object.
	 * 
	 * @param cookieName  name of the cookie to set.
	 * @param cookieValue value of the cookie to set.
	 * @param age         maximum age in seconds for the cookie to set.
	 * @param resp        {@link HttpServletResponse} object to set cookie to.
	 */
	public static void setCookie(String cookieName, String cookieValue, int age, HttpServletResponse resp) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(age);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		resp.addCookie(cookie);
	}

	/**
	 * Returns request URI with query string if any from provided
	 * {@link HttpServletRequest} object.
	 * 
	 */
	public static String getCurrentRequestUrl(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString == null) {
			return request.getRequestURI();
		} else {
			return request.getRequestURI() + "?" + queryString;
		}
	}

	private static boolean isExist(String value) {
		return value != null && !"".equals(value.trim());
	}

	private WebUtils() {
	}
}
