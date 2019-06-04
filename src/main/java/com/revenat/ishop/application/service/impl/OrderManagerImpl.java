package com.revenat.ishop.application.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.Checks;

@Service
public class OrderManagerImpl extends PageableResultService implements OrderManager {
	private static final String CART_REQUIRED_MSG_CODE = "message.error.requiredCartWithProducts";
	
	private AuthenticationService authService;
	private OrderService orderService;
	private FeedbackService feedbackService;
	private Transformer transformer;

	@Autowired
	public OrderManagerImpl(AuthenticationService authService, OrderService orderService,
			FeedbackService feedbackService, Transformer transformer) {
		this.authService = authService;
		this.orderService = orderService;
		this.feedbackService = feedbackService;
		this.transformer = transformer;
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
		
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				cart.clear();
				feedbackService.sendNewOrderNotification(userAccount.getEmail(), clientSession.getClientLocale(), order);
			}
		});
		
		return order.getId();
	}
	
	@Transactional(readOnly=true)
	@Override
	public OrderDTO getById(long id, ClientSession clientSession) {
		OrderDTO order = getOrder(id);
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		validateOrderOwnership(order, userAccount);
		
		return order;
	}
	
	@Transactional(readOnly=true)
	@Override
	public List<OrderDTO> findByClient(ClientSession clientSession, int page, int limit) {
		validateParams(page, limit);
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		return orderService.findByAccountId(userAccount.getId(), page, limit);
	}

	@Transactional(readOnly=true)
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
				"Can not create new order: provided shopping cart is null or empty",
				CART_REQUIRED_MSG_CODE
				);
	}
}
