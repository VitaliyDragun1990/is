package com.revenat.ishop.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebFilter("/*")
public class ErrorHandlerFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger("ErrorhandlerFilter");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
//			throwException();
			chain.doFilter(request, response);
		} catch (Exception e) {
			String requestUrl = ((HttpServletRequest) request).getRequestURI();
			LOGGER.log(Level.SEVERE, "Request " + requestUrl + "failed: " + e.getMessage(), e);
			((HttpServletResponse)response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
		}
	}

	private void throwException() {
		throw new RuntimeException();
	}

	@Override
	public void destroy() {
	}
}
