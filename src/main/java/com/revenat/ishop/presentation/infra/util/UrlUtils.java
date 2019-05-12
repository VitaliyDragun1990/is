package com.revenat.ishop.presentation.infra.util;

/**
 * Utility class with convenient methods to define what kind of URL spevified
 * URL is.
 * 
 * @author Vitaly Dragun
 *
 */
public class UrlUtils {

	public static boolean isAjaxUrl(String url) {
		return url.startsWith("/ajax/");
	}

	public static boolean isAjaxJsonUrl(String url) {
		return url.startsWith("/ajax/json/");
	}

	public static boolean isAjaxHtmlUrl(String url) {
		return url.startsWith("/ajax/html/");
	}

	public static boolean isStaticUrl(String url) {
		return url.startsWith("/static/");
	}

	public static boolean isMediaUrl(String url) {
		return url.startsWith("/media/");
	}

	private UrlUtils() {
	}
}
