package com.revenat.ishop.servlet.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.config.Constants.Fragment;
import com.revenat.ishop.entity.Order;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class LoadMoreOrdersController extends AbstractController {
	private static final long serialVersionUID = 146402650416631222L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int requestedPage = getPage(req);
		List<Order> orders = getOrderManager().findByUser(getClientSession(req), requestedPage, Constants.MAX_ORDERS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.ORDERS, orders);
		
		RoutingUtils.forwardToFragment(Fragment.ORDER_LIST, req, resp);
	}
}
