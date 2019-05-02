package com.revenat.ishop.repository;

import java.util.List;

import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.search.criteria.ProductCriteria;

/**
 * This interface represents repository for managing CRUD operations with
 * {@link Producer} entity.
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
	List<Producer> getAll();
	
	List<Producer> getByCriteria(ProductCriteria criteria);
}
