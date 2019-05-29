package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.JDBCRepository;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.CollectionItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;
import com.revenat.ishop.infrastructure.repository.builder.FindProducersByCriteriaSQLBuilder;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Producer} entity.
 * 
 * @author Vitaly Dragun
 *
 */
@JDBCRepository
public interface ProducerRepository {

	/**
	 * Returns all {@link Producer} entites from the datastore.
	 * 
	 * @return {@link List} containing {@link Producer} entities or empty list if
	 *         there are no producers in the datastore.
	 */
	@Select("SELECT * FROM producer ORDER BY name")
	@CollectionItem(Producer.class)
	List<Producer> findAll();

	/**
	 * Returns all {@link Producer} entites which satisfy specified {@code criteria}
	 * 
	 * @param criteria search criteria producers should satisfy
	 */
	@Select(value="", sqlBuilderClass=FindProducersByCriteriaSQLBuilder.class)
	@CollectionItem(Producer.class)
	List<Producer> findByCriteria(ProductCriteria criteria);
}
