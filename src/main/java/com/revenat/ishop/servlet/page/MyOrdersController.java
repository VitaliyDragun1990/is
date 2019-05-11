package com.revenat.ishop.servlet.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;
import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.config.Constants.Page;
import com.revenat.ishop.entity.Order;

public class MyOrdersController extends AbstractController {
	private static final long serialVersionUID = -4414626894042562584L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Order> userOrders = getOrderManager().findByClient(getClientSession(req), 1, Constants.MAX_ORDERS_PER_HTML_PAGE);
		int totalOrderCount = getOrderManager().coundByClient(getClientSession(req));
		int totalPageCount = getTotalPageCount(totalOrderCount, Constants.MAX_ORDERS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.ORDERS, userOrders);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		
		RoutingUtils.forwardToPage(Page.MY_ORDERS, req, resp);
	}
}
