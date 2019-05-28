package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.ProductRepository;
import com.revenat.ishop.infrastructure.util.Checks;

@Transactional(readOnly=true)
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public List<Product> findProducts(int page, int limit) {
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		return productRepository.findAll(limit, offset);
	}

	@Override
	public List<Product> findProductsByCategory(String categoryUrl, int page, int limit) {
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		return productRepository.findByCategory(categoryUrl, limit, offset);
	}
	
	@Override
	public List<Product> findProductsByCriteria(ProductCriteria criteria, int page, int limit) {
		validate(page, limit);
		int offset = calculateOffset(page, limit);
		return productRepository.findByCriteria(criteria, limit, offset);
	}

	private static int calculateOffset(int page, int productsPerPage) {
		return (page - 1) * productsPerPage;
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
