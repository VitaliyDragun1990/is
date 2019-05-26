package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.jdbc.Transactional;
import com.revenat.ishop.infrastructure.repository.CategoryRepository;

@Transactional(readOnly=true)
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.getAll();
	}
	
	@Override
	public List<Category> getCategoriesByCriteria(ProductCriteria criteria) {
		return categoryRepository.getByCriteria(
				ProductCriteria.byProducers(criteria.getQuery(), criteria.getProducerIds())
				);
	}

}
