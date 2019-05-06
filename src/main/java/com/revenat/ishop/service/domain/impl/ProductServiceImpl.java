package com.revenat.ishop.service.domain.impl;

import java.util.List;

import com.revenat.ishop.entity.Category;
import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.repository.CategoryRepository;
import com.revenat.ishop.repository.ProducerRepository;
import com.revenat.ishop.repository.ProductRepository;
import com.revenat.ishop.search.criteria.ProductCriteria;
import com.revenat.ishop.service.domain.ProductService;

public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProducerRepository producerRepository;
	
	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			ProducerRepository producerRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.producerRepository = producerRepository;
	}

	@Override
	public List<Product> getProducts(int page, int limit) {
		int offset = calculateOffset(page, limit);
		return productRepository.getAll(offset, limit);
	}

	@Override
	public List<Product> getProductsByCategory(String categoryUrl, int page, int limit) {
		int offset = calculateOffset(page, limit);
		return productRepository.getByCategory(categoryUrl, offset, limit);
	}
	
	@Override
	public List<Product> getProductsByCriteria(ProductCriteria criteria, int page, int limit) {
		int offset = calculateOffset(page, limit);
		return productRepository.getByCriteria(criteria, offset, limit);
	}

	private int calculateOffset(int page, int productsPerPage) {
		return (page - 1) * productsPerPage;
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

	@Override
	public List<Producer> getAllProducers() {
		return producerRepository.getAll();
	}
	
	@Override
	public List<Producer> getProducersByCriteria(ProductCriteria criteria) {
		return producerRepository.getByCriteria(
				ProductCriteria.byCategories(criteria.getQuery(), criteria.getCategoryIds())
				);
	}

	@Override
	public int countAllProducts() {
		return productRepository.countAll();
	}

	@Override
	public int countProductsByCategory(String categoryUrl) {
		return productRepository.countByCategory(categoryUrl);
	}

	@Override
	public int countProductsByCriteria(ProductCriteria criteria) {
		return productRepository.countByCriteria(criteria);
	}
}
