package com.revenat.ishop.infrastructure.repository.builder;

public class FindCategoriesByCriteriaSQLBuilder extends AbstractProductCriteriaSQLBuilder {
	private static final String GET_CATEGORIES_BY_CRITERIA_TEMPLATE = "SELECT c.id, c.name, c.url, "
			+ "CAST(count(prod.id) AS INTEGER) AS product_count "
			+ "FROM category AS c LEFT OUTER JOIN "
			+ "(SELECT p.id, p.category_id FROM product AS p INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "%s) "
			+ "AS prod ON c.id = prod.category_id "
			+ "GROUP BY c.id, c.name, c.url "
			+ "ORDER BY c.name";
	
	@Override
	protected String getSqlTemplate() {
		return GET_CATEGORIES_BY_CRITERIA_TEMPLATE;
	}

}
