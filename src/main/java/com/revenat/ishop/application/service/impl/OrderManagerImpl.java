package com.revenat.ishop.application.service.impl;


import java.util.List;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;
import com.revenat.ishop.infrastructure.exception.security.AccessDeniedException;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.framework.factory.TransactionSynchronizationManager;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.Checks;

@Component
public class OrderManagerImpl implements OrderManager {
	@Autowired
	private AuthenticationService authService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private FeedbackService feedbackService;
	@Autowired
	private Transformer transformer;
	
	public OrderManagerImpl() {
	}
	
	public OrderManagerImpl(AuthenticationService authService, OrderService orderService,
			FeedbackService feedbackService) {
		this.authService = authService;
		this.orderService = orderService;
		this.feedbackService = feedbackService;
	}

	@Transactional(readOnly=false)
	@Override
	public long placeOrder(ClientSession clientSession) {
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		ShoppingCart cart = clientSession.getShoppingCart();
		validateShoppingCart(cart);
		
		OrderDTO order = orderService.createOrder(
				transformer.untransfrom(cart.getItems(), OrderItem.class),
				userAccount.getId());
		
		TransactionSynchronizationManager.addSynchronization(() -> {
			cart.clear();
			feedbackService.sendNewOrderNotification(userAccount.getEmail(), order);
		});
		
		return order.getId();
	}
	
	@Transactional
	@Override
	public OrderDTO getById(long id, ClientSession clientSession) {
		OrderDTO order = getOrder(id);
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		validateOrderOwnership(order, userAccount);
		
		return order;
	}
	
	@Transactional
	@Override
	public List<OrderDTO> findByClient(ClientSession clientSession, int page, int limit) {
		validateParams(page, limit);
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		return orderService.findByAccountId(userAccount.getId(), page, limit);
	}

	@Transactional
	@Override
	public int coundByClient(ClientSession clientSession) {
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		return orderService.countByAccountId(userAccount.getId());
	}
	
	private OrderDTO getOrder(long id) {
		OrderDTO order = orderService.findById(id);
		if (order == null) {
			throw new ResourceNotFoundException(String.format("Order with id: %d not found", id));
		}
		return order;
	}
	
	private static void validateOrderOwnership(OrderDTO order, ClientAccount userAccount) {
		if (!order.getAccountId().equals(userAccount.getId())) {
			throw new AccessDeniedException(
					String.format("Account with id=%d is not the owner for the order with id=%d",
							userAccount.getId(), order.getId()));
		}
	}

	private static void validateShoppingCart(ShoppingCart cart) {
		Checks.validateCondition(cart != null && !cart.isEmpty(),
				"Can not create new order: provided shopping cart is null or empty");
	}
	
	private static void validateParams(int page, int limit) {
		Checks.checkParam(page >= 1, "page number can not be less that 1: %d", page);
		Checks.checkParam(limit >= 1, "limit can not be less that 1: %d", limit);
	}
}
