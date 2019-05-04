package com.revenat.ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;
import com.revenat.ishop.config.Constants.Page;

public class SignInController extends AbstractController {
	private static final long serialVersionUID = -6406166047812875130L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (getAuthService().isAuthenticated(req.getSession())) {
			RoutingUtils.redirect("/my-orders", resp);
		} else {
			RoutingUtils.forwardToPage(Page.SIGN_IN, req, resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (getAuthService().isAuthenticated(req.getSession())) {
			RoutingUtils.redirect("/my-orders", resp);
		} else {
			RoutingUtils.redirect(getAuthService().getAuthenticationUrl(), resp);
		}
	}
}
