package com.revenat.ishop.presentation.controller.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.presentation.controller.AbstractAuthenticationController;
import com.revenat.ishop.presentation.infra.config.Constants.Attribute;
import com.revenat.ishop.presentation.infra.config.Constants.Page;
import com.revenat.ishop.presentation.infra.config.Constants.URL;
import com.revenat.ishop.presentation.infra.util.RoutingUtils;

public class SignInController extends AbstractAuthenticationController {
	private static final String REDIRECT_URL_PARAM = "redirectUrl";
	private static final long serialVersionUID = -6406166047812875130L;
	
	public SignInController(AuthenticationService authService) {
		super(authService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (authService.isAuthenticated(getClientSession(req))) {
			RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
		} else {
			RoutingUtils.forwardToPage(Page.SIGN_IN, req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (authService.isAuthenticated(getClientSession(req))) {
			RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
		} else {
			setRedirectUrl(req);
			RoutingUtils.redirect(authService.getAuthenticationUrl(), resp);
		}
	}
	
	private void setRedirectUrl(HttpServletRequest req) {
		HttpSession userSession = req.getSession();
		if (userSession.getAttribute(Attribute.REDIRECT_URL) == null) {
			String redirectUrl = getRedirectUrl(req);
			userSession.setAttribute(Attribute.REDIRECT_URL, redirectUrl);
		}
	}

	private String getRedirectUrl(HttpServletRequest req) {
		String redirectUrlParam = req.getParameter(REDIRECT_URL_PARAM);
		if (redirectUrlParam != null) {
			return redirectUrlParam;
		} else {
			return URL.MY_ORDERS;
		}
	}
}
