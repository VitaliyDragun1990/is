package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.domain.entity.Producer;
import com.revenat.ishop.application.domain.search.criteria.ProductCriteria;

public interface ProducerService {

	List<Producer> getAllProducers();

	List<Producer> getProducersByCriteria(ProductCriteria criteria);
}
