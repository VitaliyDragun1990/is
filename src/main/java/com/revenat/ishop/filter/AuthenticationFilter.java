package com.revenat.ishop.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.config.Constants.URL;
import com.revenat.ishop.model.ClientSession;
import com.revenat.ishop.service.ServiceManager;
import com.revenat.ishop.service.application.AuthenticationService;
import com.revenat.ishop.util.web.RoutingUtils;
import com.revenat.ishop.util.web.UrlUtils;
import com.revenat.ishop.util.web.WebUtils;

/**
 * This filter is responsible for authentication checking. If client, who made
 * such request, is not authenticated, they will be redirected to sing-in page
 * or get appropriate response.
 * 
 * @author Vitaly Dragun
 *
 */
public class AuthenticationFilter extends AbstractFilter {
	private AuthenticationService authService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServiceManager serviceManager = (ServiceManager) filterConfig.getServletContext()
				.getAttribute(Constants.Attribute.SERVICE_MANAGER);
		authService = serviceManager.getAuthService();
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
