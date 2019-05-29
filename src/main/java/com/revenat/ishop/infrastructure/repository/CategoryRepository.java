package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.JDBCRepository;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.CollectionItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;
import com.revenat.ishop.infrastructure.repository.builder.FindCategoriesByCriteriaSQLBuilder;

/**
 * This interface represents repository responsible for performing CRUD operations on
 * {@link Category} entity.
 * 
 * @author Vitaly Dragun
 *
 */
@JDBCRepository
public interface CategoryRepository {

	/**
	 * Returns all {@link Category} entites from the datastore.
	 * 
	 * @return {@link List} containing {@link Category} entities or empty list if
	 *         there are no categories in the datastore.
	 */
	@Select("SELECT * FROM category ORDER BY name")
	@CollectionItem(Category.class)
	List<Category> findAll();
	
	/**
	 * Returns all {@link Category} entities which satisfy specified {@code criteria}
	 */
	@Select(value="", sqlBuilderClass=FindCategoriesByCriteriaSQLBuilder.class)
	@CollectionItem(Category.class)
	List<Category> findByCriteria(ProductCriteria criteria);
}
