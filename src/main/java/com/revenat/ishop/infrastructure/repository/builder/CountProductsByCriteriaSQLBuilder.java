package com.revenat.ishop.infrastructure.repository.builder;

public class CountProductsByCriteriaSQLBuilder extends AbstractProductCriteriaSQLBuilder {
	private static final String COUNT_PRODUCTS_BY_CRITERIA_TEMPLATE = "SELECT count(*) AS count "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "%s";
	@Override
	protected String getSqlTemplate() {
		return COUNT_PRODUCTS_BY_CRITERIA_TEMPLATE;
	}

}
