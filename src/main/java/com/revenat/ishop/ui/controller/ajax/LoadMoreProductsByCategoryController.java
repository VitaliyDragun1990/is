package com.revenat.ishop.ui.controller.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.ui.config.Constants;
import com.revenat.ishop.ui.config.Constants.Attribute;
import com.revenat.ishop.ui.config.Constants.Fragment;
import com.revenat.ishop.ui.config.Constants.URL;
import com.revenat.ishop.ui.controller.AbstractProductController;
import com.revenat.ishop.ui.util.RoutingUtils;

public class LoadMoreProductsByCategoryController extends AbstractProductController {
	private static final long serialVersionUID = 1L;
	private static final int SUBSTRING_INDEX = URL.AJAX_MORE_PRODUCTS.length();
	
	public LoadMoreProductsByCategoryController(ProductService productService) {
		super(productService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String categoryUrl = getCategoryName(req.getRequestURI());
		int requestedPage = getPage(req);
		List<Product> products =
				productService.getProductsByCategory(categoryUrl, requestedPage, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.PRODUCTS, products);
		
		RoutingUtils.forwardToFragment(Fragment.PRODUCT_LIST, req, resp);
	}
	
	private String getCategoryName(String requestURI) {
		return requestURI.substring(SUBSTRING_INDEX);
	}
}
