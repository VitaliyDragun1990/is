package com.revenat.ishop.presentation.controller.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.domain.entity.Product;
import com.revenat.ishop.application.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.presentation.controller.AbstractProductController;
import com.revenat.ishop.presentation.form.SearchForm;
import com.revenat.ishop.presentation.infra.config.Constants;
import com.revenat.ishop.presentation.infra.config.Constants.Attribute;
import com.revenat.ishop.presentation.infra.config.Constants.Fragment;
import com.revenat.ishop.presentation.infra.util.RoutingUtils;

public class LoadMoreProductsForSearchResultController extends AbstractProductController {
	private static final long serialVersionUID = 1L;
	private static final String PRODUCER = "producer";
	private static final String CATEGORY = "category";
	private static final String QUERY = "query";
	
	public LoadMoreProductsForSearchResultController(ProductService productService) {
		super(productService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SearchForm searchForm = getSearchForm(req);
		ProductCriteria productCriteria = searchForm.toProductCriteria();
		
		int requestedPage = getPage(req);
		List<Product> products = productService
				.getProductsByCriteria(productCriteria, requestedPage, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.PRODUCTS, products);
		
		RoutingUtils.forwardToFragment(Fragment.PRODUCT_LIST, req, resp);
	}

	private SearchForm getSearchForm(HttpServletRequest req) {
		return new SearchForm(
				req.getParameter(QUERY),
				req.getParameterValues(CATEGORY),
				req.getParameterValues(PRODUCER));
	}
}
