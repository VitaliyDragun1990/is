package com.revenat.ishop.application.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.application.transform.transformer.Transformer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.ProductRepository;

@Service
@Transactional(readOnly=true)
public class ProductServiceImpl extends PageableResultService implements ProductService {
	
	private ProductRepository productRepository;
	private Transformer transformer;
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, Transformer transformer) {
		this.productRepository = productRepository;
		this.transformer = transformer;
	}

	@Override
	public List<ProductDTO> findProducts(int page, int limit) {
		validateParams(page, limit);
//		int offset = calculateOffset(page, limit);
		Pageable sortById = new PageRequest(page-1, limit, Direction.ASC, "id");
		return transformer.transfrom(productRepository.findAll(sortById), ProductDTO.class);
	}

	@Override
	public List<ProductDTO> findProductsByCategory(String categoryUrl, int page, int limit) {
		validateParams(page, limit);
//		int offset = calculateOffset(page, limit);
		Pageable sortByNameAsc = new PageRequest(page-1, limit, Direction.ASC, "name");
		return transformer.transfrom(productRepository.findByCategoryUrl(categoryUrl, sortByNameAsc), ProductDTO.class);
	}
	
	@Override
	public List<ProductDTO> findProductsByCriteria(ProductCriteria criteria, int page, int limit) {
		validateParams(page, limit);
//		int offset = calculateOffset(page, limit);
		Pageable sortByNameAsc = new PageRequest(page-1, limit, Direction.ASC, "name");
		return transformer.transfrom(productRepository.findByCriteria(criteria, sortByNameAsc), ProductDTO.class);
	}

//	private static int calculateOffset(int page, int productsPerPage) {
//		return (page - 1) * productsPerPage;
//	}
	
	@Override
	public int countAllProducts() {
		return (int) productRepository.count();
	}

	@Override
	public int countProductsByCategory(String categoryUrl) {
		return productRepository.countByCategoryUrl(categoryUrl);
	}

	@Override
	public int countProductsByCriteria(ProductCriteria criteria) {
		return (int) productRepository.countByCriteria(criteria);
	}
}
