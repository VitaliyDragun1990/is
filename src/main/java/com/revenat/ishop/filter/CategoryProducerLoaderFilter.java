package com.revenat.ishop.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.service.ProductService;
import com.revenat.ishop.service.impl.ServiceManager;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.entity.Category;
import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.form.SearchForm;

/**
 * This filter is responsible for retrieving particular {@link Category} and
 * {@link Producer} entities which satisfy some criteria and storing them as
 * attributes into client's request object for each client request. This
 * approach is suitable if the criteria by which entities are searched and
 * retrieved can potentially change at each request.
 * 
 * ATTENTION: This filter exists only for demostration. It is not used in
 * current project.
 * 
 * @author Vitaly Dragun
 *
 */
public class CategoryProducerLoaderFilter extends AbstractFilter {

	private ProductService productService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		productService = ServiceManager.getInstance(filterConfig.getServletContext()).getProductService();
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
