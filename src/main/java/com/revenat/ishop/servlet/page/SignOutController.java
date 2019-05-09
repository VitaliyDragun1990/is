package com.revenat.ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants.URL;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class SignOutController extends AbstractController {
	private static final long serialVersionUID = 1594075883613571119L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		getAuthService().logout(getClientSession(req));
		RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
	}
}