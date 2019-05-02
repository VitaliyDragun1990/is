package com.revenat.ishop.servlet.ajax;

import static com.revenat.ishop.config.Constants.Fragment;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class LoadMoreProductsByCategoryController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final int SUBSTRING_INDEX = "/ajax/html/more/products".length();
	private static final String PRODUCTS = "products";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String categoryUrl = getCategoryName(req.getRequestURI());
		int requestedPage = getPage(req);
		List<Product> products =
				getProductService().getProductsByCategory(categoryUrl, requestedPage, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(PRODUCTS, products);
		
		RoutingUtils.forwardToFragment(Fragment.PRODUCT_LIST, req, resp);
	}
	
	private String getCategoryName(String requestURI) {
		return requestURI.substring(SUBSTRING_INDEX);
	}
}
