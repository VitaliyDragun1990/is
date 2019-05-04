package com.revenat.ishop.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;
import com.revenat.ishop.config.Constants.Page;

public class MyOrdersController extends AbstractController {
	private static final long serialVersionUID = -4414626894042562584L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RoutingUtils.forwardToPage(Page.MY_ORDERS, req, resp);
	}
}
