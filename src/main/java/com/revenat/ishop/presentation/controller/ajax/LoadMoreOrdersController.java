package com.revenat.ishop.presentation.controller.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.presentation.config.Constants;
import com.revenat.ishop.presentation.config.Constants.Attribute;
import com.revenat.ishop.presentation.config.Constants.Fragment;
import com.revenat.ishop.presentation.controller.AbstractOrderController;
import com.revenat.ishop.presentation.util.RoutingUtils;

public class LoadMoreOrdersController extends AbstractOrderController {
	private static final long serialVersionUID = 146402650416631222L;
	
	public LoadMoreOrdersController(OrderManager orderManager) {
		super(orderManager);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int requestedPage = getPage(req);
		List<OrderDTO> orders = orderManager.findByClient(getClientSession(req), requestedPage, Constants.MAX_ORDERS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.ORDERS, orders);
		
		RoutingUtils.forwardToFragment(Fragment.ORDER_LIST, req, resp);
	}
}
