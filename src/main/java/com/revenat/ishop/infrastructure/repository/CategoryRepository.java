package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import com.revenat.ishop.domain.entity.Category;

/**
 * This interface represents repository responsible for performing CRUD operations on
 * {@link Category} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface CategoryRepository extends Repository<Category, Integer>, SearchCategoryRepository {

	/**
	 * Returns all {@link Category} entites from the datastore.
	 * 
	 * @return {@link List} containing {@link Category} entities or empty list if
	 *         there are no categories in the datastore.
	 */
	List<Category> findAll(Sort sort);
}
