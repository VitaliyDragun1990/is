package com.revenat.ishop.application.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revenat.ishop.application.dto.ProducerDTO;
import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

@Service
@Transactional(readOnly=true)
public class ProducerServiceImpl implements ProducerService {
	private ProducerRepository producerRepository;
	private Transformer transformer;
	
	@Autowired
	public ProducerServiceImpl(ProducerRepository producerRepository, Transformer transformer) {
		this.producerRepository = producerRepository;
		this.transformer = transformer;
	}

	public ProducerServiceImpl(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	@Override
	public List<ProducerDTO> findAllProducers() {
		return transformer.transfrom(producerRepository.findAll(new Sort("name")), ProducerDTO.class);
	}
	
	@Override
	public List<ProducerDTO> findProducersByCriteria(ProductCriteria criteria) {
		return transformer.transfrom(
				producerRepository.findByCriteria(ProductCriteria.byCategories(criteria.getQuery(), criteria.getCategoryIds())),
				ProducerDTO.class);
	}

}
