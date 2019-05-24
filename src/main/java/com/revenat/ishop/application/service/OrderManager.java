package com.revenat.ishop.application.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.revenat.ishop.application.model.ClientAccount;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.domain.model.ShoppingCart;
import com.revenat.ishop.domain.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;
import com.revenat.ishop.infrastructure.exception.security.AccessDeniedException;
import com.revenat.ishop.infrastructure.util.Checks;

public class OrderManager {
	private final AuthenticationService authService;
	private final OrderService orderService;
	
	public OrderManager(AuthenticationService authService, OrderService orderService) {
		this.authService = authService;
		this.orderService = orderService;
	}
	
	public long placeOrder(ClientSession clientSession) {
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		ShoppingCart cart = clientSession.getShoppingCart();
		validateShoppingCart(cart);
		
		Order order = orderService.createOrder(toOrderItems(cart.getItems()), userAccount.getId());
		
		cart.clear();
		
		return order.getId();
	}
	
	public Order findById(long id, ClientSession clientSession) {
		Order order = findOrder(id);
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		validateOrderOwnership(order, userAccount);
		
		return order;
	}
	
	private Order findOrder(long id) {
		Order order = orderService.getById(id);
		if (order == null) {
			throw new ResourceNotFoundException(String.format("Order with id: %d not found", id));
		}
		return order;
	}
	
	public List<Order> findByClient(ClientSession clientSession, int page, int limit) {
		validateParams(page, limit);
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		return orderService.getByAccountId(userAccount.getId(), page, limit);
	}

	public int coundByClient(ClientSession clientSession) {
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		return orderService.countByAccountId(userAccount.getId());
	}

	private List<OrderItem> toOrderItems(Collection<ShoppingCartItem> cartItems) {
		List<OrderItem> orderItems = new ArrayList<>();
		for (ShoppingCartItem cartItem : cartItems) {
			OrderItem orderItem = toOrderItem(cartItem);
			orderItems.add(orderItem);
		}
		return orderItems;
	}

	private OrderItem toOrderItem(ShoppingCartItem cartItem) {
		OrderItem orderItem = new OrderItem();
		orderItem.setProduct(cartItem.getProduct());
		orderItem.setQuantity(cartItem.getQuantity());
		return orderItem;
	}
	
	private static void validateOrderOwnership(Order order, ClientAccount userAccount) {
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
