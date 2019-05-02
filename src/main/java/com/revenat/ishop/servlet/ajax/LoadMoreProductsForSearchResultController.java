package com.revenat.ishop.servlet.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Fragment;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.form.SearchForm;
import com.revenat.ishop.search.criteria.ProductCriteria;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class LoadMoreProductsForSearchResultController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final String PRODUCTS = "products";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SearchForm searchForm = getSearchForm(req);
		ProductCriteria productCriteria = searchForm.toProductCriteria();
		
		int requestedPage = getPage(req);
		List<Product> products = getProductService()
				.getProductsByCriteria(productCriteria, requestedPage, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(PRODUCTS, products);
		
		RoutingUtils.forwardToFragment(Fragment.PRODUCT_LIST, req, resp);
	}
}
