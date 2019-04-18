package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {
	private static final String SIGN_IN_URL = "/sign-in";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String requestUrl = req.getRequestURI();
		if (req.getSession().getAttribute("IS_AUTHENTICATED") != null) {
			chain.doFilter(request, response);
		} else {
			req.getSession().setAttribute("REDIRECT_URL_AFTER_SIGNIN", requestUrl);
			((HttpServletResponse)response).sendRedirect(SIGN_IN_URL);
		}
	}

	@Override
	public void destroy() {}

}
