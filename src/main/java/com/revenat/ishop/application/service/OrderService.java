package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;

public interface OrderService {
	
	Order createOrder(List<OrderItem> orderItems, int accountId);
	
	Order findById(long id);
	
	List<Order> findByAccountId(int accountId, int page, int limit);
	
	int countByAccountId(int accountId);
}
