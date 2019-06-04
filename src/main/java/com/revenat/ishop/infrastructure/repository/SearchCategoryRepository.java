package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface SearchCategoryRepository {

	/**
	 * Returns all {@link Category} entities which satisfy specified {@code criteria}
	 */
	List<Category> findByCriteria(ProductCriteria criteria);
}
