package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;

public interface OrderService {
	
	Order createOrder(List<OrderItem> orderItems, int accountId);
	
	Order getById(long id);
	
	List<Order> getByAccountId(int accountId, int page, int limit);
	
	int countByAccountId(int accountId);
}
