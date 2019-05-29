package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;

@Component
@Transactional(readOnly=true)
public class ProducerServiceImpl implements ProducerService {
	@Autowired
	private ProducerRepository producerRepository;

	public ProducerServiceImpl() {
	}
	
	public ProducerServiceImpl(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	@Override
	public List<Producer> findAllProducers() {
		return producerRepository.findAll();
	}
	
	@Override
	public List<Producer> findProducersByCriteria(ProductCriteria criteria) {
		return producerRepository.findByCriteria(
				ProductCriteria.byCategories(criteria.getQuery(), criteria.getCategoryIds())
				);
	}

}
