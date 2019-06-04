package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import com.revenat.ishop.domain.entity.Producer;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Producer} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ProducerRepository extends Repository<Producer, Integer>, SearchProducerRepository {

	/**
	 * Returns all {@link Producer} entites from the datastore.
	 * 
	 * @return {@link List} containing {@link Producer} entities or empty list if
	 *         there are no producers in the datastore.
	 */
	List<Producer> findAll(Sort sort);
}
