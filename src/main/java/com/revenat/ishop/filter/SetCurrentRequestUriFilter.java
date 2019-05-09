package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.util.web.WebUtils;

/**
 * The sole purpose of this filter is to store current request URL into client's
 * {@link HttpSession} object for some further operation on it.
 * 
 * @author Vitaly Dragun
 *
 */
public class SetCurrentRequestUriFilter extends AbstractFilter {

	@Override
	void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.getSession().setAttribute(Attribute.CURRENT_REQUEST_URL, WebUtils.getCurrentRequestUrl(request));
		chain.doFilter(request, response);
	}
}
