package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface ProductService {

	List<Product> findProducts(int page, int limit);
	
	List<Product> findProductsByCategory(String categoryUrl, int page, int limit);
	
	List<Product> findProductsByCriteria(ProductCriteria criteria, int page, int limit);
	
	int countAllProducts();
	
	int countProductsByCategory(String categoryUrl);
	
	int countProductsByCriteria(ProductCriteria criteria);
}
