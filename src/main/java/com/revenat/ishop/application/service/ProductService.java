package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface ProductService {

	List<ProductDTO> findProducts(int page, int limit);
	
	List<ProductDTO> findProductsByCategory(String categoryUrl, int page, int limit);
	
	List<ProductDTO> findProductsByCriteria(ProductCriteria criteria, int page, int limit);
	
	int countAllProducts();
	
	int countProductsByCategory(String categoryUrl);
	
	int countProductsByCriteria(ProductCriteria criteria);
}
