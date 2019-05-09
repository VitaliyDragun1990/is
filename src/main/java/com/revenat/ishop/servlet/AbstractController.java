package com.revenat.ishop.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.form.ProductForm;
import com.revenat.ishop.form.SearchForm;
import com.revenat.ishop.model.ClientSession;
import com.revenat.ishop.service.ServiceManager;
import com.revenat.ishop.service.application.AuthenticationService;
import com.revenat.ishop.service.application.OrderManager;
import com.revenat.ishop.service.application.ShoppingCartService;
import com.revenat.ishop.service.application.SocialService;
import com.revenat.ishop.service.domain.ProductService;

public class AbstractController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private ProductService productService;
	private ShoppingCartService cartService;
	private SocialService socialService; // TODO: remove socialService access from here
	private AuthenticationService authService;
	private OrderManager orderManager;
	
	@Override
	public void init() throws ServletException {
		ServiceManager serviceManager = ServiceManager.getInstance(getServletContext());
		productService = serviceManager.getProductService();
		cartService = serviceManager.getShoppingCartService();
		socialService = serviceManager.getSocialService();
		authService = serviceManager.getAuthService();
		orderManager = serviceManager.getOrderManager();
	}

	protected final ProductService getProductService() {
		return productService;
	}
	
	protected final ShoppingCartService getShoppingCartService() {
		return cartService;
	}
	
	protected final SocialService getSocialService() {
		return socialService;
	}
	
	protected final AuthenticationService getAuthService() {
		return authService;
	}
	
	protected final OrderManager getOrderManager() {
		return orderManager;
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
	
	protected final ClientSession getClientSession(HttpServletRequest request) {
		return (ClientSession) request.getSession().getAttribute(Attribute.CLIENT_SESSION);
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