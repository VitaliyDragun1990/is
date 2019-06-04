package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.revenat.ishop.domain.entity.OrderItem;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link OrderItem} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
	
	/**
	 * Returns all {@link OrderItem} entities which belong to order with specified
	 * {@code orderId}
	 * 
	 * @param orderId id of the order to search order items for
	 */
	List<OrderItem> findByOrderId(long orderId);
}
