package com.revenat.ishop.application.service.impl;

import java.util.List;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.ProductRepository;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;

@Component
@Transactional(readOnly=true)
public class ProductServiceImpl extends PageableResultService implements ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private Transformer transformer;
	
	public ProductServiceImpl() {
	}
	
	public ProductServiceImpl(ProductRepository productRepository, Transformer transformer) {
		this.productRepository = productRepository;
		this.transformer = transformer;
	}

	@Override
	public List<ProductDTO> findProducts(int page, int limit) {
		validateParams(page, limit);
		int offset = calculateOffset(page, limit);
		return transformer.transfrom(productRepository.findAll(limit, offset), ProductDTO.class);
	}

	@Override
	public List<ProductDTO> findProductsByCategory(String categoryUrl, int page, int limit) {
		validateParams(page, limit);
		int offset = calculateOffset(page, limit);
		return transformer.transfrom(productRepository.findByCategory(categoryUrl, limit, offset), ProductDTO.class);
	}
	
	@Override
	public List<ProductDTO> findProductsByCriteria(ProductCriteria criteria, int page, int limit) {
		validateParams(page, limit);
		int offset = calculateOffset(page, limit);
		return transformer.transfrom(productRepository.findByCriteria(criteria, limit, offset), ProductDTO.class);
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
}
