package com.revenat.ishop.servlet.page;

import static com.revenat.ishop.config.Constants.Page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class ProductsByCategoryController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final int SUBSTRING_INDEX = "/products".length();
	private static final String PRODUCTS = "products";
	private static final String SELECTED_CATEGORY_URL = "selectedCategoryUrl";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String categoryUrl = getCategoryName(req.getRequestURI());
		List<Product> products =
				getProductService().getProductsByCategory(categoryUrl, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		int totalProductCount = getProductService().countProductsByCategory(categoryUrl);
		int totalPageCount = getTotalPageCount(totalProductCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(PRODUCTS, products);
		req.setAttribute(SELECTED_CATEGORY_URL, categoryUrl);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		
		RoutingUtils.forwardToPage(Page.PRODUCTS, req, resp);
	}
	
	private String getCategoryName(String requestURI) {
		return requestURI.substring(SUBSTRING_INDEX);
	}
}
