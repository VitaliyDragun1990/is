package com.revenat.ishop.service;

import java.util.List;

import com.revenat.ishop.entity.Category;
import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.search.criteria.ProductCriteria;

public interface ProductService {

	List<Product> getProducts(int page, int limit);
	
	List<Product> getProductsByCategory(String categoryUrl, int page, int limit);
	
	List<Product> getProductsByCriteria(ProductCriteria criteria, int page, int limit);
	
	List<Category> getAllCategories();
	
	List<Category> getCategoriesByCriteria(ProductCriteria criteria);
	
	List<Producer> getAllProducers();
	
	List<Producer> getProducersByCriteria(ProductCriteria criteria);
	
	int countAllProducts();
	
	int countProductsByCategory(String categoryUrl);
	
	int countProductsByCriteria(ProductCriteria criteria);
}
