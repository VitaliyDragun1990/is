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
import com.revenat.ishop.form.SearchForm;
import com.revenat.ishop.search.criteria.ProductCriteria;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class SearchController extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SearchForm searchForm = getSearchForm(req);
		ProductCriteria productCriteria = searchForm.toProductCriteria();
		
		List<Product> products = getProductService()
				.getProductsByCriteria(productCriteria, 1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		int totalProductCount = getProductService().countProductsByCriteria(productCriteria);
		int totalPageCount = getTotalPageCount(totalProductCount, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
		
		req.setAttribute(Attribute.PRODUCTS, products);
		req.setAttribute(Attribute.TOTAL_PAGE_COUNT, totalPageCount);
		req.setAttribute(Attribute.PRODUCT_COUNT, totalProductCount);
		req.setAttribute(Attribute.SEARCH_FORM, searchForm);
		
		RoutingUtils.forwardToPage(Page.SEARCH_RESULT, req, resp);
	}
	
}
