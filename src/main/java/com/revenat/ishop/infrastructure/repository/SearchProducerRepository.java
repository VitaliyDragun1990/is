package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface SearchProducerRepository {

	/**
	 * Returns all {@link Producer} entites which satisfy specified {@code criteria}
	 * 
	 * @param criteria search criteria producers should satisfy
	 */
	List<Producer> findByCriteria(ProductCriteria criteria);
}
