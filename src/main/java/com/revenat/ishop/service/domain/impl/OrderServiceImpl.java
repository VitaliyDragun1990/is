package com.revenat.ishop.service.domain.impl;

import java.util.List;

import com.revenat.ishop.entity.Order;
import com.revenat.ishop.entity.OrderItem;
import com.revenat.ishop.repository.OrderRepository;
import com.revenat.ishop.service.domain.OrderService;

public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@Override
	public Order createOrder(List<OrderItem> orderItems, int accountId) {
		Order order = new Order();
		order.setAccountId(accountId);
		order.setItems(orderItems);
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
}
