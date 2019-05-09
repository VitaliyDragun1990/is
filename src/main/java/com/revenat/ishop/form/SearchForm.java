package com.revenat.ishop.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.revenat.ishop.search.criteria.ProductCriteria;

/**
 * This component represents search form which user fills in to make complex search
 * request.
 * 
 * @author Vitaly Dragun
 *
 */
public class SearchForm {
	private final String query;
	private final List<Integer> categories;
	private final List<Integer> producers;

	public SearchForm(String query, String[] categories, String[] producers) {
		this.query = query == null ? "" : query.trim();
		this.categories = parseFrom(categories);
		this.producers = parseFrom(producers);
	}
	
	public ProductCriteria toProductCriteria() {
		return ProductCriteria.byCategoriesAndProducers(query, categories, producers);
	}

	private List<Integer> parseFrom(String[] entries) {
		List<Integer> result = new ArrayList<>();
		if (entries != null) {
			for (String entry : entries) {
				try {
					result.add(Integer.parseInt(entry));
				} catch (NumberFormatException e) {
					// silently ignore incorect value
				}
			}
		}
		return result;
	}
	
	public boolean isEmpty() {
		return query.isEmpty() && categories.isEmpty() && producers.isEmpty();
	}

	public String getQuery() {
		return query;
	}

	public List<Integer> getCategories() {
		return Collections.unmodifiableList(categories);
	}

	public List<Integer> getProducers() {
		return Collections.unmodifiableList(producers);
	}

	@Override
	public String toString() {
		return String.format("SearchForm [query=%s, categories=%s, producers=%s]", query, categories, producers);
	}
}
