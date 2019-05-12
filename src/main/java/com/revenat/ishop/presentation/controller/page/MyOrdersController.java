package com.revenat.ishop.presentation.controller.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.domain.entity.Order;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.presentation.controller.AbstractOrderController;
import com.revenat.ishop.presentation.infra.config.Constants;
import com.revenat.ishop.presentation.infra.config.Constants.Attribute;
import com.revenat.ishop.presentation.infra.config.Constants.Page;
import com.revenat.ishop.presentation.infra.util.RoutingUtils;

public class MyOrdersController extends AbstractOrderController {
	private static final long serialVersionUID = -4414626894042562584L;
	
	public MyOrdersController(OrderManager orderManager) {
		super(orderManager);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Order> userOrders = orderManager.findByClient(getClientSession(req), 1, Constants.MAX_ORDERS_PER_HTML_PAGE);
		int totalOrderCount = orderManager.coundByClient(getClientSession(req));
		int totalPageCount = getTotalPageCount(totalOrderCount, Constants.MAX_ORDERS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.ORDERS, userOrders);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		
		RoutingUtils.forwardToPage(Page.MY_ORDERS, req, resp);
	}
}
