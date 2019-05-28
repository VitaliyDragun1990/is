package com.revenat.ishop.infrastructure.repository.builder;

public class FindProductsByCriteriaSQLBuilder extends AbstractProductCriteriaSQLBuilder {
	private static final String GET_PRODUCTS_BY_CRITERIA_TEMPLATE = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ " %s "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";

	@Override
	protected String getSqlTemplate() {
		return GET_PRODUCTS_BY_CRITERIA_TEMPLATE;
	}

}
