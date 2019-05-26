package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface CategoryService {

	List<Category> findAllCategories();

	List<Category> findCategoriesByCriteria(ProductCriteria criteria);
}
