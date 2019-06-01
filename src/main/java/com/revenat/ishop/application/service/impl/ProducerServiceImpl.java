package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.dto.ProducerDTO;
import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

@Component
@Transactional(readOnly=true)
public class ProducerServiceImpl implements ProducerService {
	@Autowired
	private ProducerRepository producerRepository;
	@Autowired
	private Transformer transformer;

	public ProducerServiceImpl() {
	}
	
	public ProducerServiceImpl(ProducerRepository producerRepository, Transformer transformer) {
		this.producerRepository = producerRepository;
		this.transformer = transformer;
	}

	public ProducerServiceImpl(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	@Override
	public List<ProducerDTO> findAllProducers() {
		return transformer.transfrom(producerRepository.findAll(), ProducerDTO.class);
	}
	
	@Override
	public List<ProducerDTO> findProducersByCriteria(ProductCriteria criteria) {
		return transformer.transfrom(
				producerRepository.findByCriteria(ProductCriteria.byCategories(criteria.getQuery(), criteria.getCategoryIds())),
				ProducerDTO.class);
	}

}
