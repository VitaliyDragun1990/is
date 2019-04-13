package com.revenat.ishop.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtils {

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
	
	public static void setCookie(String cookieName, String cookieValue, int age, HttpServletResponse resp) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(age);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		resp.addCookie(cookie);
	}

	private static boolean isExist(String value) {
		return value != null && !"".equals(value.trim());
	}
	
	private WebUtils() {
	}
}
