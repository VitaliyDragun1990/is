package com.revenat.ishop.service.application;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.revenat.ishop.entity.Order;
import com.revenat.ishop.entity.OrderItem;
import com.revenat.ishop.exception.AccessDeniedException;
import com.revenat.ishop.exception.ResourceNotFoundException;
import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ClientAccount;
import com.revenat.ishop.model.ClientSession;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.service.domain.OrderService;

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
		if (cart == null || cart.isEmpty()) {
			throw new ValidationException("Can not create new order: provided shopping cart is null or empty");
		}
		
		Order order = orderService.createOrder(toOrderItems(cart.getItems()), userAccount.getId());
		
		cart.clear();
		
		return order.getId();
	}
	
	public Order findById(long id, ClientSession clientSession) {
		Order order = orderService.getById(id);
		if (order == null) {
			throw new ResourceNotFoundException(String.format("Order with id: %d not found", id));
		}
		ClientAccount userAccount = authService.getAuthenticatedUserAccount(clientSession);
		if (!order.getAccountId().equals(userAccount.getId())) {
			throw new AccessDeniedException(
					String.format("Account with id=%d is not the owner for the order with id=%d",
							userAccount.getId(), order.getId()));
		}
		
		return order;
	}
	
	public List<Order> findByClient(ClientSession clientSession, int page, int limit) {
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
}
