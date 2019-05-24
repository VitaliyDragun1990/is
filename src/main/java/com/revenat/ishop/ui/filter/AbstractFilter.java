package com.revenat.ishop.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.ui.util.UrlUtils;

public abstract class AbstractFilter implements Filter {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// do nothing
	}

	@Override
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (isRequestToStaticResource(req)) {
			chain.doFilter(request, response);
		} else {
			doFilter(req, resp, chain);
		}
	}

	private boolean isRequestToStaticResource(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return UrlUtils.isMediaUrl(requestURI) || UrlUtils.isStaticUrl(requestURI);
	}

	abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	@Override
	public void destroy() {
		// do nothing
	}
}
