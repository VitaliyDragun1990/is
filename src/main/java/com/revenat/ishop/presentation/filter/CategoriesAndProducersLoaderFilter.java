package com.revenat.ishop.presentation.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.domain.entity.Category;
import com.revenat.ishop.application.domain.entity.Producer;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.presentation.form.SearchForm;
import com.revenat.ishop.presentation.infra.config.Constants.Attribute;

/**
 * This filter is responsible for retrieving particular {@link Category} and
 * {@link Producer} entities which satisfy some criteria and storing them as
 * attributes into client's request object for each client request. This
 * approach is suitable if the criteria by which entities are searched and
 * retrieved can potentially change at each request.
 * 
 * @author Vitaly Dragun
 *
 */
public class CategoriesAndProducersLoaderFilter extends AbstractFilter {

	private final ProductService productService;

	public CategoriesAndProducersLoaderFilter(ProductService productService) {
		this.productService = productService;
	}

	@Override
	void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		SearchForm searchForm = getSearchForm(request);
		List<Category> allCategories;
		List<Producer> allProducers;
		if (searchForm.isEmpty()) {
			allCategories = productService.getAllCategories();
			allProducers = productService.getAllProducers();
		} else {
			allCategories = productService.getCategoriesByCriteria(searchForm.toProductCriteria());
			allProducers = productService.getProducersByCriteria(searchForm.toProductCriteria());
		}
		request.setAttribute(Attribute.FILTER_CATEGORIES, allCategories);
		request.setAttribute(Attribute.FILTER_PRODUCERS, allProducers);
		chain.doFilter(request, response);
	}

	private final SearchForm getSearchForm(HttpServletRequest req) {
		String query = req.getParameter("query");
		String[] categories = req.getParameterValues("category");
		String[] producers = req.getParameterValues("producer");

		return new SearchForm(query, categories, producers);
	}
}
