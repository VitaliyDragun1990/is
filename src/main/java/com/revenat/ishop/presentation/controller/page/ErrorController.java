package com.revenat.ishop.presentation.controller.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.presentation.controller.AbstractController;
import com.revenat.ishop.presentation.infra.config.Constants.Page;
import com.revenat.ishop.presentation.infra.util.RoutingUtils;

public class ErrorController extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RoutingUtils.forwardToPage(Page.ERROR, req, resp);
	}
}
