package com.revenat.ishop.service.application;


import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.entity.Order;
import com.revenat.ishop.exception.AccessDeniedException;
import com.revenat.ishop.exception.ResourceNotFoundException;
import com.revenat.ishop.model.CurrentAccount;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.service.domain.OrderService;

public class OrderManager {
	private final ShoppingCartService shoppingCartService;
	private final AuthenticationService authService;
	private final OrderService orderService;
	
	public OrderManager(ShoppingCartService shoppingCartService, AuthenticationService authService,
			OrderService orderService) {
		this.shoppingCartService = shoppingCartService;
		this.authService = authService;
		this.orderService = orderService;
	}
	
	public long placeOrder(HttpSession userSession, HttpServletResponse resp) {
		CurrentAccount userAccount = authService.getAuthenticatedUserAccount(userSession);
		ShoppingCart cart = shoppingCartService.getShoppingCart(userSession);
		long orderId = orderService.createOrder(cart, userAccount.getId()).getId();
		
		shoppingCartService.clearShoppingCart(userSession, resp);
		
		return orderId;
	}
	
	public Order findById(long id, HttpSession userSession) {
		Order order = orderService.getById(id);
		if (order == null) {
			throw new ResourceNotFoundException(String.format("Order with id: %d not found", id));
		}
		CurrentAccount userAccount = authService.getAuthenticatedUserAccount(userSession);
		if (!order.getAccountId().equals(userAccount.getId())) {
			throw new AccessDeniedException(
					String.format("Account with id=%d is not the owner for the order with id=%d",
							userAccount.getId(), order.getId()));
		}
		
		return order;
	}
	
	public List<Order> findByUser(HttpSession userSession, int page, int limit) {
		CurrentAccount userAccount = authService.getAuthenticatedUserAccount(userSession);
		return orderService.getByAccountId(userAccount.getId(), page, limit);
	}
	
	public int coundByUser(HttpSession userSession) {
		CurrentAccount userAccount = authService.getAuthenticatedUserAccount(userSession);
		return orderService.countByAccountId(userAccount.getId());
	}
}
