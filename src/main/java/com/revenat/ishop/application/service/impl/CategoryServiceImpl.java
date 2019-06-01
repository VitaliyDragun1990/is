package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.dto.CategoryDTO;
import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.CategoryRepository;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

@Component
@Transactional(readOnly=true)
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private Transformer transformer;

	public CategoryServiceImpl() {
	}
	
	public CategoryServiceImpl(CategoryRepository categoryRepository, Transformer transformer) {
		this.categoryRepository = categoryRepository;
		this.transformer = transformer;
	}

	@Override
	public List<CategoryDTO> findAllCategories() {
		return transformer.transfrom(categoryRepository.findAll(), CategoryDTO.class);
	}
	
	@Override
	public List<CategoryDTO> findCategoriesByCriteria(ProductCriteria criteria) {
		return transformer.transfrom(
				categoryRepository.findByCriteria(ProductCriteria.byProducers(criteria.getQuery(), criteria.getProducerIds())),
				CategoryDTO.class);
	}
}
