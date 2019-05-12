package com.revenat.ishop.presentation.controller.page;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.impl.CredentialsFactory;
import com.revenat.ishop.presentation.controller.AbstractAuthenticationController;
import com.revenat.ishop.presentation.infra.config.Constants.Attribute;
import com.revenat.ishop.presentation.infra.config.Constants.URL;
import com.revenat.ishop.presentation.infra.util.RoutingUtils;

public class SocialLoginController extends AbstractAuthenticationController {
	private static final long serialVersionUID = -9143781612631892699L;

	public SocialLoginController(AuthenticationService authService) {
		super(authService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String authToken = req.getParameter("code");
		if (authToken != null) {
			authService.authenticate(CredentialsFactory.fromAuthToken(authToken), getClientSession(req));
			redirectToTargetPage(req, resp);
		} else {
			LOGGER.warn("Parameter 'code' not found");
			redirectToSignInPage(req, resp);
		}
	}
	
	private void redirectToTargetPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		RoutingUtils.redirect(getRedirectUrl(req), resp);
	}

	private void redirectToSignInPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		req.getSession().removeAttribute(Attribute.REDIRECT_URL);
		RoutingUtils.redirect(URL.SIGN_IN, resp);
	}

	private String getRedirectUrl(HttpServletRequest req) throws IOException {
		HttpSession userSession = req.getSession();
		String redirectUrl = userSession.getAttribute(Attribute.REDIRECT_URL).toString();
		userSession.removeAttribute(Attribute.REDIRECT_URL);
		return URLDecoder.decode(redirectUrl, "UTF-8");
	}
}
