package com.revenat.ishop.service.domain.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.revenat.ishop.entity.Order;
import com.revenat.ishop.entity.OrderItem;
import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.repository.OrderRepository;
import com.revenat.ishop.service.domain.OrderService;

public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@Override
	public Order createOrder(ShoppingCart cart, int accountId) {
		if (cart == null || cart.isEmpty()) {
			throw new ValidationException("Can not create new order: provided shopping cart is null or empty");
		}
		Order order = new Order();
		order.setAccountId(accountId);
		order.setItems(toOrderItems(cart.getItems()));
		orderRepository.save(order);
		
		return order;
	}
	
	@Override
	public Order getById(long id) {
		return orderRepository.getById(id);
	}
	
	@Override
	public List<Order> getByAccountId(int accountId, int page, int limit) {
		int offset = calculateOffset(page, limit);
		return orderRepository.getByAccountId(accountId, offset, limit);
	}
	
	@Override
	public int countByAccountId(int accountId) {
		return orderRepository.countByAccountId(accountId);
	}
	
	private int calculateOffset(int page, int productsPerPage) {
		return (page - 1) * productsPerPage;
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
