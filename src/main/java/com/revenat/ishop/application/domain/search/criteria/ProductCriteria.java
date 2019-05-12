package com.revenat.ishop.application.domain.search.criteria;

import java.util.Collections;
import java.util.List;

import com.revenat.ishop.application.domain.entity.Product;
import com.revenat.ishop.application.infra.util.Checks;

/**
 * This value object represents criteria object to search for {@link Product} with.
 * 
 * @author Vitaly Dragun
 *
 */
public class ProductCriteria {
	private final String query;
	private final List<Integer> categoryIds;
	private final List<Integer> producerIds;

	private ProductCriteria(String query, List<Integer> categories, List<Integer> producers) {
		checkParams(query, categories, producers);
		this.query = query;
		this.categoryIds = categories;
		this.producerIds = producers;
	}
	
	private static void checkParams(String query, List<Integer> categories, List<Integer> producers) {
		Checks.checkParam(query != null, "Search query cannot be null");
		Checks.checkParam(categories != null, "List with categories id can not be null");
		Checks.checkParam(producers != null, "List with producers id can not be null");
	}

	public static ProductCriteria byCategories(String query, List<Integer> categoryIds) {
		return new ProductCriteria(query, categoryIds, Collections.emptyList());
	}
	
	public static ProductCriteria byProducers(String query, List<Integer> producerIds) {
		return new ProductCriteria(query, Collections.emptyList(), producerIds);
	}
	
	public static ProductCriteria byCategoriesAndProducers(String query, List<Integer> categoryIds, List<Integer> producerIds) {
		return new ProductCriteria(query, categoryIds, producerIds);
	}

	public String getQuery() {
		return query;
	}

	public List<Integer> getCategoryIds() {
		return Collections.unmodifiableList(categoryIds);
	}

	public List<Integer> getProducerIds() {
		return Collections.unmodifiableList(producerIds);
	}

	@Override
	public String toString() {
		return String.format("ProductCriteria [query=%s, categories=%s, producers=%s]", query, categoryIds, producerIds);
	}
}
