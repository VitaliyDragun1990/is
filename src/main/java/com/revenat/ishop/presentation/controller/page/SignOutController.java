package com.revenat.ishop.presentation.controller.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.presentation.config.Constants.URL;
import com.revenat.ishop.presentation.controller.AbstractAuthenticationController;
import com.revenat.ishop.presentation.util.RoutingUtils;

public class SignOutController extends AbstractAuthenticationController {
	private static final long serialVersionUID = 1594075883613571119L;
	
	public SignOutController(AuthenticationService authService) {
		super(authService);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		authService.logout(getClientSession(req));
		RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
	}
}