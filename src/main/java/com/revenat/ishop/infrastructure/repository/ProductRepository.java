package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.JDBCRepository;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.CollectionItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;
import com.revenat.ishop.infrastructure.repository.builder.CountProductsByCriteriaSQLBuilder;
import com.revenat.ishop.infrastructure.repository.builder.FindProductsByCriteriaSQLBuilder;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Product} entity.
 * 
 * @author Vitaly Dragun
 *
 */
@JDBCRepository
public interface ProductRepository {
	public static final String GET_PRODUCT_BY_ID = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "WHERE p.id = ? ORDER BY p.id";
	public static final String GET_PRODUCTS_BY_CATEGORY = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id " + "WHERE c.url = ? "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	public static final String GET_ALL_PRODUCTS = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";

	/**
	 * Returns how many {@link Product} instances datastore contains.
	 */
	@Select("SELECT count(*) as count FROM product")
	int countAll();

	/**
	 * Returns how many {@link Product} instances which belong to particular
	 * category datastore contains.
	 * 
	 * @param categoryUrl string that explicitely determines particular category
	 */
	@Select("SELECT product_count AS count FROM category AS c WHERE c.url = ?")
	int countByCategory(String categoryUrl);

	/**
	 * Returns how many {@link Product} entities that satisfy given search criteria
	 * datastore contains.
	 * 
	 * @param criteria search criteria object
	 */
	@Select(value="",
			sqlBuilderClass=CountProductsByCriteriaSQLBuilder.class)
	int countByCriteria(ProductCriteria criteria);

	/**
	 * Returns {@link Product} entity with specified {@code id} property value or
	 * {@code null} if there is no product with such id.
	 * 
	 * @param id unique {@code id} property value
	 */
	@Select(GET_PRODUCT_BY_ID)
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
	@Select(GET_PRODUCTS_BY_CATEGORY)
	@CollectionItem(Product.class)
	List<Product> findByCategory(String categoryUrl, int limit, int offset);

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
	@Select(value="", sqlBuilderClass=FindProductsByCriteriaSQLBuilder.class)
	@CollectionItem(Product.class)
	List<Product> findByCriteria(ProductCriteria criteria, int limit, int offset);

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
	@Select(GET_ALL_PRODUCTS)
	@CollectionItem(Product.class)
	List<Product> findAll(int limit, int offset);
}
