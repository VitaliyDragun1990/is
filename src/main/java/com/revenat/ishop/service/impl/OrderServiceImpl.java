package com.revenat.ishop.service.impl;

import com.revenat.ishop.repository.ProductRepository;
import com.revenat.ishop.service.OrderService;

class OrderServiceImpl implements OrderService {
	private final ProductRepository productRepository;

	public OrderServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
}
