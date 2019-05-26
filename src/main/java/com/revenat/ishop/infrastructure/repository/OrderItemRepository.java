package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.OrderItem;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link OrderItem} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface OrderItemRepository {

	/**
	 * Saves specified {@link OrderItem}
	 * 
	 * @param orderItem order item to save
	 * @return saved order item
	 */
	OrderItem save(OrderItem orderItem);

	/**
	 * Saves all provided {@link OrderItem} entities
	 * 
	 * @param orderItems order items to save
	 */
	void saveAll(Iterable<OrderItem> orderItems);

	/**
	 * Returns all {@link OrderItem} entities which belong to order with specified
	 * {@code orderId}
	 * 
	 * @param orderId id of the order to search order items for
	 */
	List<OrderItem> findByOrderId(long orderId);
}
