package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.dto.CategoryDTO;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface CategoryService {

	List<CategoryDTO> findAllCategories();

	List<CategoryDTO> findCategoriesByCriteria(ProductCriteria criteria);
}
