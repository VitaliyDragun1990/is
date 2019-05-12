package com.revenat.ishop.persistence.repository;

import java.util.List;

import com.revenat.ishop.application.domain.entity.Order;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Order} entity.
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
