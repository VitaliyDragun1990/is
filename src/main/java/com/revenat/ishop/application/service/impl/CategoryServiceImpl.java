package com.revenat.ishop.application.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revenat.ishop.application.dto.CategoryDTO;
import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.CategoryRepository;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

@Service
@Transactional(readOnly=true)
public class CategoryServiceImpl implements CategoryService {
	private CategoryRepository categoryRepository;
	private Transformer transformer;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository, Transformer transformer) {
		this.categoryRepository = categoryRepository;
		this.transformer = transformer;
	}

	@Override
	public List<CategoryDTO> findAllCategories() {
		return transformer.transfrom(categoryRepository.findAll(new Sort("name")), CategoryDTO.class);
	}
	
	@Override
	public List<CategoryDTO> findCategoriesByCriteria(ProductCriteria criteria) {
		return transformer.transfrom(
				categoryRepository.findByCriteria(ProductCriteria.byProducers(criteria.getQuery(), criteria.getProducerIds())),
				CategoryDTO.class);
	}
}
