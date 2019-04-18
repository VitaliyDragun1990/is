package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

//@WebFilter(filterName = "TestFilter", urlPatterns="/*")
public class TestFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// some init work here
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		req.setAttribute("CURRENT_URL", req.getRequestURI());
		System.out.println("Before doFilter() call");
		chain.doFilter(request, response);
		System.out.println("After doFilter() call");
	}

	@Override
	public void destroy() {
		// some cleanup work here
	}

}
