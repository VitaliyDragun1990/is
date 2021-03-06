package com.revenat.ishop.presentation.controller.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.presentation.config.Constants;
import com.revenat.ishop.presentation.config.Constants.Attribute;
import com.revenat.ishop.presentation.config.Constants.Page;
import com.revenat.ishop.presentation.config.Constants.URL;
import com.revenat.ishop.presentation.controller.AbstractProductController;
import com.revenat.ishop.presentation.util.RoutingUtils;

public class ProductsByCategoryController extends AbstractProductController {
	private static final long serialVersionUID = 1L;
	private static final int SUBSTRING_INDEX = URL.ALL_PRODUCTS.length();
	
	public ProductsByCategoryController(ProductService productService) {
		super(productService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String categoryUrl = getCategoryName(req.getRequestURI());
		List<ProductDTO> products = productService.findProductsByCategory(categoryUrl, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		int totalProductCount = productService.countProductsByCategory(categoryUrl);
		int totalPageCount = getTotalPageCount(totalProductCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.PRODUCTS, products);
		req.setAttribute(Attribute.SELECTED_CATEGORY_URL, categoryUrl);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		
		RoutingUtils.forwardToPage(Page.PRODUCTS, req, resp);
	}
	
	private String getCategoryName(String requestURI) {
		return requestURI.substring(SUBSTRING_INDEX);
	}
}
