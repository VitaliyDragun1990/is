package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface ProducerService {

	List<Producer> findAllProducers();

	List<Producer> findProducersByCriteria(ProductCriteria criteria);
}
