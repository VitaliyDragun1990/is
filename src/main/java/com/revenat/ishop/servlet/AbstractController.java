package com.revenat.ishop.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.revenat.ishop.form.ProductForm;
import com.revenat.ishop.form.SearchForm;
import com.revenat.ishop.service.OrderService;
import com.revenat.ishop.service.ProductService;
import com.revenat.ishop.service.ShoppingCartService;
import com.revenat.ishop.service.impl.ServiceManager;

public class AbstractController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProductService productService;
	private OrderService orderService;
	private ShoppingCartService cartService;
	
	@Override
	public void init() throws ServletException {
		productService = ServiceManager.getInstance(getServletContext()).getProductService();
		orderService = ServiceManager.getInstance(getServletContext()).getOrderService();
		cartService = ServiceManager.getInstance(getServletContext()).getShoppingCartService();
	}

	protected final ProductService getProductService() {
		return productService;
	}
	
	protected final OrderService getOrderService() {
		return orderService;
	}
	
	protected final ShoppingCartService getShoppingCartService() {
		return cartService;
	}
	
	protected final int getTotalPageCount(int totalItemsCount, int itemsPerPage) {
		int totalPageCount = totalItemsCount / itemsPerPage;
		if (totalPageCount * itemsPerPage != totalItemsCount) {
			totalPageCount++;
		}
		
		return totalPageCount;
	}
	
	protected final int getPage(HttpServletRequest req) {
		try {
			return Integer.parseInt(req.getParameter("page"));
		} catch (NumberFormatException e) {
			// If page format not an integer, return 1 page
			return 1;
		}
	}
	
	protected final SearchForm getSearchForm(HttpServletRequest req) {
		String query = req.getParameter("query");
		String[] categories = req.getParameterValues("category");
		String[] producers = req.getParameterValues("producer");
		
		return new SearchForm(query, categories, producers);
	}
	
	protected ProductForm getProductForm(HttpServletRequest req) {
		return new ProductForm(
				Integer.parseInt(req.getParameter("productId")),
				Integer.parseInt(req.getParameter("quantity")));
	}
}
