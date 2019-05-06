package com.revenat.ishop.servlet.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.config.Constants.Page;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class AllProductsController extends AbstractController {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Product> products = getProductService().getProducts(1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		int totalProductCount = getProductService().countAllProducts();
		int totalPageCount = getTotalPageCount(totalProductCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.PRODUCTS, products);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		
		RoutingUtils.forwardToPage(Page.PRODUCTS, req, resp);
	}
}
