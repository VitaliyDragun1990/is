package com.revenat.ishop.presentation.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.presentation.config.Constants.Attribute;
import com.revenat.ishop.presentation.config.Constants.URL;
import com.revenat.ishop.presentation.util.RoutingUtils;
import com.revenat.ishop.presentation.util.UrlUtils;
import com.revenat.ishop.presentation.util.WebUtils;

/**
 * This filter is responsible for authentication checking. If client, who made
 * such request, is not authenticated, they will be redirected to sing-in page
 * or get appropriate response.
 * 
 * @author Vitaly Dragun
 *
 */
public class AuthenticationFilter extends AbstractFilter {
	private final AuthenticationService authService;

	public AuthenticationFilter(AuthenticationService authService) {
		this.authService = authService;
	}

	@Override
	void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ClientSession clientSession = (ClientSession) request.getSession().getAttribute(Attribute.CLIENT_SESSION);
		if (authService.isAuthenticated(clientSession)) {
			chain.doFilter(request, response);
		} else {
			processUnauthorizedRequest(request, response);
		}
	}

	private void processUnauthorizedRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String requestUrl = WebUtils.getCurrentRequestUrl(request);
		if (UrlUtils.isAjaxUrl(requestUrl)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			RoutingUtils.sendHtmlFragment("401", response);
		} else {
			request.getSession().setAttribute(Attribute.REDIRECT_URL, requestUrl);
			RoutingUtils.redirect(URL.SIGN_IN, response);
		}
	}
}
