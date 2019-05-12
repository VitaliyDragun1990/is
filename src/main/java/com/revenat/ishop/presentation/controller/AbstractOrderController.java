package com.revenat.ishop.presentation.controller;

import com.revenat.ishop.application.service.OrderManager;

public abstract class AbstractOrderController extends AbstractPaginationController {
	private static final long serialVersionUID = 3735511643682815868L;

	protected final OrderManager orderManager;

	public AbstractOrderController(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
}
