package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Product} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ProductRepository {

	/**
	 * Returns how many {@link Product} instances datastore contains.
	 */
	int countAll();

	/**
	 * Returns how many {@link Product} instances which belong to particular
	 * category datastore contains.
	 * 
	 * @param categoryUrl string that explicitely determines particular category
	 */
	int countByCategory(String categoryUrl);

	/**
	 * Returns how many {@link Product} entities that satisfy given search criteria
	 * datastore contains.
	 * 
	 * @param criteria search criteria object
	 */
	int countByCriteria(ProductCriteria criteria);

	/**
	 * Returns {@link Product} entity with specified {@code id} property value or
	 * {@code null} if there is no product with such id.
	 * 
	 * @param id unique {@code id} property value
	 */
	Product getById(Integer id);

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
	List<Product> getByCategory(String categoryUrl, int offset, int limit);

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
	List<Product> getByCriteria(ProductCriteria criteria, int offset, int limit);

	/**
	 * Returns list of {@link Product} entities using specified {@code offset} and
	 * {@code limit} parameters to to limit number of entities to be returned.
	 * 
	 * @param offset position from which product entites should be returned, value
	 *               {@code <= 0} means return from the beginning.
	 * @param limit  max number of entites to be returned, {@code < 0} means return
	 *               all matches.
	 * @return list with products or an empty list if there is no products that
	 *         satisfy specified {@code offset} and/or {@code limit} parameters.
	 */
	List<Product> getAll(int offset, int limit);
}
