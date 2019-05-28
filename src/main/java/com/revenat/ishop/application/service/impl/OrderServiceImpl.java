package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.OrderItemRepository;
import com.revenat.ishop.infrastructure.repository.OrderRepository;
import com.revenat.ishop.infrastructure.util.Checks;

@Transactional(readOnly=true)
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
	}

	@Transactional(readOnly=false)
	@Override
	public Order createOrder(List<OrderItem> orderItems, int accountId) {
		Order order = new Order();
		order.setAccountId(accountId);
		orderRepository.save(order);
		
		orderItems.forEach(item -> item.setOrderId(order.getId()));
		orderItemRepository.saveAll(orderItems);
		
		order.setItems(orderItems);
		
		return order;
	}
	
	@Override
	public Order findById(long id) {
		Order order =  orderRepository.findById(id);
		if (order != null) {
			loadOrderItemsForOrder(order);
		}
		return order;
	}
	
	@Override
	public List<Order> findByAccountId(int accountId, int page, int limit) {
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		List<Order> orders = orderRepository.findByAccountId(accountId, limit, offset);

		orders.forEach(this::loadOrderItemsForOrder);
		
		return orders;
	}
	
	@Override
	public int countByAccountId(int accountId) {
		return orderRepository.countByAccountId(accountId);
	}
	
	private void loadOrderItemsForOrder(Order order) {
		// Get all orderItems for such order
		List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
		// Add these orderItems to order
		order.setItems(orderItems);
	}
	
	private static int calculateOffset(int page, int productsPerPage) {
		return (page - 1) * productsPerPage;
	}
	
	private static void validate(int page, int limit) {
		Checks.checkParam(page >= 1, "page number can not be less that 1: %d", page);
		Checks.checkParam(limit >= 1, "limit can not be less that 1: %d", limit);
	}
}
