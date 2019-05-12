package com.revenat.ishop.presentation.controller;

import com.revenat.ishop.application.service.ProductService;

public abstract class AbstractProductController extends AbstractPaginationController {
	private static final long serialVersionUID = -6752559073141644756L;
	
	protected final ProductService productService;

	public AbstractProductController(ProductService productService) {
		this.productService = productService;
	}
}
