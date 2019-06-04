package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.revenat.ishop.domain.entity.Order;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Order} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

	Order findById(Long id);

	List<Order> findByAccountId(Integer accountId, Pageable pageable);

	int countByAccountId(Integer accountId);
}
