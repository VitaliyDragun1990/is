package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.domain.entity.Category;
import com.revenat.ishop.application.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.persistence.repository.CategoryRepository;

class CategoryServiceImpl implements CategoryService {
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