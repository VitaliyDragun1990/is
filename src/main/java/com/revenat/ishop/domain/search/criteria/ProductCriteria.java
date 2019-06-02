package com.revenat.ishop.domain.search.criteria;

import java.util.Collections;
import java.util.List;

import com.revenat.ishop.domain.entity.Product;

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
		this.query = query;
		this.categoryIds = categories;
		this.producerIds = producers;
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
