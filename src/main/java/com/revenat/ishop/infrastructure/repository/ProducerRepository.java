package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Producer} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ProducerRepository {

	/**
	 * Returns all {@link Producer} entites from the datastore.
	 * 
	 * @return {@link List} containing {@link Producer} entities or empty list if
	 *         there are no producers in the datastore.
	 */
	List<Producer> findAll();

	/**
	 * Returns all {@link Producer} entites which satisfy specified {@code criteria}
	 * 
	 * @param criteria search criteria producers should satisfy
	 */
	List<Producer> findByCriteria(ProductCriteria criteria);
}
