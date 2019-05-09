package com.revenat.ishop.repository;

import java.util.List;

import com.revenat.ishop.entity.OrderItem;

/**
 * This interface represents repository responsible for performing CRUD operations on
 * {@link OrderItem} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface OrderItemRepository {

	void save(OrderItem orderItem);
	
	void saveAll(Iterable<OrderItem> orderItems);
	
	List<OrderItem> getByOrderId(long orderId);
}
