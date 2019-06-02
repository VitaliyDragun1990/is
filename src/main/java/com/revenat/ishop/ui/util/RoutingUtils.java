package com.revenat.ishop.ui.util;

import static com.revenat.ishop.ui.config.Constants.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Helper utility class that contains convenient methods to facilitate routing
 * operations in the UI layer.
 * 
 * @author Vitaly Dragun
 *
 */
public final class RoutingUtils {

	/**
	 * Forwards given clients request alongside with appropriate response to given
	 * JSP fragment.
	 * 
	 * @param jspFragment JSP fragment to forward to.
	 * @param req         client's request in form of {@link HttpServletRequest}
	 *                    instance.
	 * @param resp        {@link HttpServletResponse} instance that incapsulates
	 *                    response for client's request.
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void forwardToFragment(String jspFragment, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher(Directory.FRAGMENT + jspFragment).forward(req, resp);
	}

	/**
	 * Forwards given clients request alongside with appropriate response to given
	 * JSP page.
	 * 
	 * @param jspPafe JSP page to forward to.
	 * @param req     client's request in form of {@link HttpServletRequest}
	 *                instance.
	 * @param resp    {@link HttpServletResponse} instance that incapsulates
	 *                response for client's request.
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void forwardToPage(String jspPage, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("currentPage", Directory.PAGE + jspPage);
		req.getRequestDispatcher(Directory.JSP + Page.TEMPLATE).forward(req, resp);
	}

	/**
	 * Sends specified text as HTML directly to client using specified
	 * {@link HttpServletResponse} object.
	 * 
	 * @param text text that represents HTML fragment to send.
	 * @param resp {@link HttpServletResponse} object that incapsulates response for
	 *             client's request.
	 * @throws IOException
	 */
	public static void sendHtmlFragment(String text, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println(text);
		resp.getWriter().close();
	}

	/**
	 * Sends specified string as json directly to client using specified
	 * {@link HttpServletResponse} object.
	 * 
	 * @param json string that represents json object
	 * @param resp {@link HttpServletResponse} object that incapsulates response for
	 *             client's request.
	 * @throws IOException
	 */
	public static void sendJSON(String json, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json; charset=utf-8");
		resp.getWriter().println(json);
		resp.getWriter().close();
	}

	/**
	 * Sends redirect response via provided {@link HttpServletResponse} instance,
	 * using specified url location.
	 * 
	 * @param url  location URL that represents target for redirect response
	 * @param resp {@link HttpServletResponse} instance that incapsulates response
	 *             for client's request.
	 * @throws IOException
	 */
	public static void redirect(String url, HttpServletResponse resp) throws IOException {
		resp.sendRedirect(url);
	}

	private RoutingUtils() {
	}
}
