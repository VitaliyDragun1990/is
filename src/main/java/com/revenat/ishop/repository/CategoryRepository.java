package com.revenat.ishop.repository;

import java.util.List;

import com.revenat.ishop.entity.Category;
import com.revenat.ishop.search.criteria.ProductCriteria;

/**
 * This interface represents repository for managing CRUD operations with
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
	List<Category> getAll();
	
	List<Category> getByCriteria(ProductCriteria criteria);
}
