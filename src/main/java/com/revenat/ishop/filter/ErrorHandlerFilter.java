package com.revenat.ishop.filter;

import static com.revenat.ishop.config.Constants.Page;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.util.web.RoutingUtils;

public class ErrorHandlerFilter extends AbstractFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			String requestUrl = request.getRequestURI();
			LOGGER.error("Request " + requestUrl + " failed: " + e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			RoutingUtils.forwardToPage(Page.ERROR, request, response);
		}
	}
}
