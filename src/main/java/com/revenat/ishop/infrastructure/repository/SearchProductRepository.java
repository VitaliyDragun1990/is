package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface SearchProductRepository {


	/**
	 * Returns list of {@link Product} entities which satisfy some search criteria
	 * specified by provided {@code criteria} parameter, using specified
	 * {@code offset} and {@code limit} parameters to limit number of entities to be
	 * returned.
	 * 
	 * @param criteria search criteria object to search for {@link Product} entities
	 * @param offset   position from which product entites should be returned, value
	 *                 {@code <= 0} means return from the beginning.
	 * @param limit    max number of entites to be returned, {@code < 0} means
	 *                 return all matches.
	 * @return list of matching product or an empty list if there is no matches.
	 */
	List<Product> findByCriteria(ProductCriteria criteria, Pageable pageable);
	
	/**
	 * Returns how many {@link Product} entities that satisfy given search criteria
	 * datastore contains.
	 * 
	 * @param criteria search criteria object
	 */
	long countByCriteria(ProductCriteria criteria);
}
