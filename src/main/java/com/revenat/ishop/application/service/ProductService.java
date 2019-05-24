package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public interface ProductService {

	List<Product> getProducts(int page, int limit);
	
	List<Product> getProductsByCategory(String categoryUrl, int page, int limit);
	
	List<Product> getProductsByCriteria(ProductCriteria criteria, int page, int limit);
	
	int countAllProducts();
	
	int countProductsByCategory(String categoryUrl);
	
	int countProductsByCriteria(ProductCriteria criteria);
}
