package com.revenat.ishop.repository;

import java.util.List;

import com.revenat.ishop.entity.Order;

/**
 * This interface represents repository for managing CRUD operations with
 * {@link Order} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface OrderRepository {

	void save(Order order);
	
	Order getById(long id);
	
	List<Order> getByAccountId(int accountId, int offset, int limit);
	
	int countByAccountId(int accountId);
}
