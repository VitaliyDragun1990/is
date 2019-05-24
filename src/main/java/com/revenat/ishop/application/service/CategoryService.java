package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.domain.entity.Category;
import com.revenat.ishop.application.domain.search.criteria.ProductCriteria;

public interface CategoryService {

	List<Category> getAllCategories();

	List<Category> getCategoriesByCriteria(ProductCriteria criteria);
}
