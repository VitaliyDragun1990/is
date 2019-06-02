package com.revenat.ishop.ui.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.ui.config.Constants.Attribute;

/**
 * The sole purpose of this filter is to store current request URL into client's
 * {@link HttpSession} object for some further operation on it.
 * 
 * @author Vitaly Dragun
 *
 */
public class ClientLocaleDefinerFilter extends AbstractFilter {

	@Override
	void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ClientSession clientSession = (ClientSession) request.getSession().getAttribute(Attribute.CLIENT_SESSION);
		clientSession.setClientLocale(request.getLocale());
		chain.doFilter(request, response);
	}
}
