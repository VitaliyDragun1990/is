package com.revenat.ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.service.impl.CredentialsFactory;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class SocialLoginController extends AbstractController {
	private static final long serialVersionUID = -9143781612631892699L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String authToken = req.getParameter("code");
		if (authToken != null) {
			getAuthService().authenticate(CredentialsFactory.fromAuthToken(authToken), req.getSession());
			RoutingUtils.redirect("/my-orders", resp);
		} else {
			LOGGER.warn("Parameter 'code' not found");
			RoutingUtils.redirect("/sing-in", resp);
		}
	}
}
