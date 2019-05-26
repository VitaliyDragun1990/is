package com.revenat.ishop.ui.controller.page;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.form.SearchForm;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.ui.config.Constants;
import com.revenat.ishop.ui.config.Constants.Attribute;
import com.revenat.ishop.ui.config.Constants.Page;
import com.revenat.ishop.ui.controller.AbstractProductController;
import com.revenat.ishop.ui.util.RoutingUtils;

public class ProductSearchController extends AbstractProductController {
	private static final long serialVersionUID = 1L;
	private static final String PRODUCER = "producer";
	private static final String CATEGORY = "category";
	private static final String QUERY = "query";
	
	public ProductSearchController(ProductService productService) {
		super(productService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SearchForm searchForm = getSearchForm(req);
		ProductCriteria productCriteria = searchForm.toProductCriteria();
		
		List<Product> products = productService
				.findProductsByCriteria(productCriteria, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		int totalProductCount = productService.countProductsByCriteria(productCriteria);
		int totalPageCount = getTotalPageCount(totalProductCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.PRODUCTS, products);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		req.setAttribute(Attribute.PRODUCT_COUNT, totalProductCount);
		req.setAttribute(Attribute.SEARCH_FORM, searchForm);
		
		RoutingUtils.forwardToPage(Page.SEARCH_RESULT, req, resp);
	}

	private SearchForm getSearchForm(HttpServletRequest req) {
		return new SearchForm(
				req.getParameter(QUERY),
				req.getParameterValues(CATEGORY),
				req.getParameterValues(PRODUCER));
	}
}
