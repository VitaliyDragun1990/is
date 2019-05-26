package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

/**
 * This interface represents repository responsible for performing CRUD operations on
 * {@link Category} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface CategoryRepository {

	/**
	 * Returns all {@link Category} entites from the datastore.
	 * 
	 * @return {@link List} containing {@link Category} entities or empty list if
	 *         there are no categories in the datastore.
	 */
	List<Category> findAll();
	
	/**
	 * Returns all {@link Category} entities which satisfy specified {@code criteria}
	 */
	List<Category> findByCriteria(ProductCriteria criteria);
}
