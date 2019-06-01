package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.dto.ProducerDTO;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface ProducerService {

	List<ProducerDTO> findAllProducers();

	List<ProducerDTO> findProducersByCriteria(ProductCriteria criteria);
}
