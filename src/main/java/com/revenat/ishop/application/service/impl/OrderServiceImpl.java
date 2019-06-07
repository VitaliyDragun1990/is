package com.revenat.ishop.application.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.application.transform.transformer.Transformer;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.repository.OrderItemRepository;
import com.revenat.ishop.infrastructure.repository.OrderRepository;

@Service
@Transactional(readOnly=true)
public class OrderServiceImpl implements OrderService {
	
	private OrderRepository orderRepository;
	private OrderItemRepository orderItemRepository;
	private Transformer transformer;
	
	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			Transformer transformer) {
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.transformer = transformer;
	}

	@Transactional(readOnly=false)
	@Override
	public OrderDTO createOrder(List<OrderItem> orderItems, int accountId) {
		Order order = new Order();
		order.setAccountId(accountId);
		orderRepository.save(order);
		
		orderItems.forEach(item -> item.setOrderId(order.getId()));
		orderItemRepository.save(orderItems);
		
		order.setItems(orderItems);
		
		return transformer.transform(order, OrderDTO.class);
	}
	
	@Override
	public OrderDTO findById(long id) {
		Order order =  orderRepository.findById(id);
		if (order != null) {
			loadOrderItemsForOrder(order);
		}
		return transformer.transform(order, OrderDTO.class);
	}
	
	@Override
	public List<OrderDTO> findByAccountId(int accountId, int page, int limit) {
//		int offset = calculateOffset(page, limit);
		Pageable sortByIdDesc = new PageRequest(page-1, limit, Direction.DESC, "id");
		List<Order> orders = orderRepository.findByAccountId(accountId, sortByIdDesc);

		orders.forEach(this::loadOrderItemsForOrder);
		
		return transformer.transfrom(orders, OrderDTO.class);
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
	
//	private static int calculateOffset(int page, int productsPerPage) {
//		return (page - 1) * productsPerPage;
//	}
}
