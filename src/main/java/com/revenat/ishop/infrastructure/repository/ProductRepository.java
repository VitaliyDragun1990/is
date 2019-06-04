package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.revenat.ishop.domain.entity.Product;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Product} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer>, SearchProductRepository {

	/**
	 * Returns how many {@link Product} instances which belong to particular
	 * category datastore contains.
	 * 
	 * @param categoryUrl string that explicitely determines particular category
	 */
	int countByCategoryUrl(String url);

	/**
	 * Returns {@link Product} entity with specified {@code id} property value or
	 * {@code null} if there is no product with such id.
	 * 
	 * @param id unique {@code id} property value
	 */
	Product findById(Integer id);

	/**
	 * Returns list of {@link Product} entities which belongs to some category
	 * specified by provided {@code categoryUrl} parameter, using specified
	 * {@code offset} and {@code limit} parameters to limit number of entities to be
	 * returned.
	 * 
	 * @param categoryUrl string url that explicitly determines some category
	 * @param offset      position from which product entites should be returned,
	 *                    value {@code <= 0} means return from the beginning.
	 * @param limit       max number of entites to be returned, {@code < 0} means
	 *                    return all matches.
	 * @return list of matching product or an empty list if there is no matches.
	 */
	List<Product> findByCategoryUrl(String url, Pageable pageable);

}
