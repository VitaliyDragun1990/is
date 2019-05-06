package com.revenat.ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.config.Constants.Page;
import com.revenat.ishop.config.Constants.URL;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class SignInController extends AbstractController {
	private static final String REDIRECT_URL_PARAM = "redirectUrl";
	private static final long serialVersionUID = -6406166047812875130L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (getAuthService().isAuthenticated(req.getSession())) {
			RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
		} else {
			RoutingUtils.forwardToPage(Page.SIGN_IN, req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (getAuthService().isAuthenticated(req.getSession())) {
			RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
		} else {
			setRedirectUrl(req);
			RoutingUtils.redirect(getAuthService().getAuthenticationUrl(), resp);
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
