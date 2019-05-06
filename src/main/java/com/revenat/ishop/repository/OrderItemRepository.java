package com.revenat.ishop.repository;

import java.util.List;

import com.revenat.ishop.entity.OrderItem;

public interface OrderItemRepository {

	void save(OrderItem orderItem);
	
	void saveAll(Iterable<OrderItem> orderItems);
	
	List<OrderItem> getByOrderId(long orderId);
}
