package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.framework.annotation.jdbc.Transactional;
import com.revenat.ishop.infrastructure.repository.OrderRepository;
import com.revenat.ishop.infrastructure.util.Checks;

@Transactional(readOnly=true)
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	
	@Transactional(readOnly=false)
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
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		return orderRepository.getByAccountId(accountId, offset, limit);
	}
	
	@Override
	public int countByAccountId(int accountId) {
		return orderRepository.countByAccountId(accountId);
	}
	
	private static int calculateOffset(int page, int productsPerPage) {
		return (page - 1) * productsPerPage;
	}
	
	private static void validate(int page, int limit) {
		Checks.checkParam(page >= 1, "page number can not be less that 1: %d", page);
		Checks.checkParam(limit >= 1, "limit can not be less that 1: %d", limit);
	}
}
