package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.domain.entity.Category;
import com.revenat.ishop.application.domain.entity.Producer;
import com.revenat.ishop.application.domain.entity.Product;
import com.revenat.ishop.application.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.application.infra.util.Checks;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.persistence.repository.CategoryRepository;
import com.revenat.ishop.persistence.repository.ProducerRepository;
import com.revenat.ishop.persistence.repository.ProductRepository;

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
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		return productRepository.getAll(offset, limit);
	}

	@Override
	public List<Product> getProductsByCategory(String categoryUrl, int page, int limit) {
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		return productRepository.getByCategory(categoryUrl, offset, limit);
	}
	
	@Override
	public List<Product> getProductsByCriteria(ProductCriteria criteria, int page, int limit) {
		validate(page, limit);
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
	
	private static void validate(int page, int limit) {
		Checks.checkParam(page >= 1, "page number can not be less that 1: %d", page);
		Checks.checkParam(limit >= 1, "limit can not be less that 1: %d", limit);
	}
}
