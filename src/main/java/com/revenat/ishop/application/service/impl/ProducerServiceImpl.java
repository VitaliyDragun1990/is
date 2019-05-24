package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;

class ProducerServiceImpl implements ProducerService {
	private final ProducerRepository producerRepository;

	public ProducerServiceImpl(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	@Override
	public List<Producer> getAllProducers() {
		return producerRepository.getAll();
	}
	
	@Override
	public List<Producer> getProducersByCriteria(ProductCriteria criteria) {
		return producerRepository.getByCriteria(
				ProductCriteria.byCategories(criteria.getQuery(), criteria.getCategoryIds())
				);
	}

}
