package com.revenat.ishop.ui.controller.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.ui.config.Constants.Attribute;
import com.revenat.ishop.ui.config.Constants.Page;
import com.revenat.ishop.ui.config.Constants.URL;
import com.revenat.ishop.ui.controller.AbstractOrderController;
import com.revenat.ishop.ui.util.RoutingUtils;

public class OrderController extends AbstractOrderController {
	private static final long serialVersionUID = -2654221307584151283L;
	
	public OrderController(OrderManager orderManager) {
		super(orderManager);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long orderId = orderManager.placeOrder(getClientSession(req));
		
		req.getSession().setAttribute(Attribute.CURRENT_MESSAGE,
				"Order has been created successfully. Please wait for our reply.");
		RoutingUtils.redirect(URL.ORDER_WITH_ID + orderId, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long orderId = Long.parseLong(req.getParameter("id"));
		OrderDTO order = orderManager.getById(orderId, getClientSession(req));
		
		req.setAttribute(Attribute.CURRENT_ORDER, order);
		displayMessageIfAny(req);
		RoutingUtils.forwardToPage(Page.ORDER, req, resp);
	}

	private void displayMessageIfAny(HttpServletRequest req) {
		Object currentMessage = req.getSession().getAttribute(Attribute.CURRENT_MESSAGE);
		if (currentMessage != null) {
			req.getSession().removeAttribute(Attribute.CURRENT_MESSAGE);
			req.setAttribute(Attribute.CURRENT_MESSAGE, currentMessage.toString());
		}
	}
}
