package com.revenat.ishop.infrastructure.repository.builder;

public class FindProducersByCriteriaSQLBuilder extends AbstractProductCriteriaSQLBuilder {
	private static final String GET_PRODUCERS_BY_CRITERIA_TEMPLATE = "SELECT pr.id, pr.name, "
			+ "CAST(count(prod.id) AS INTEGER) AS product_count "
			+ "FROM producer AS pr LEFT OUTER JOIN "
			+ "(SELECT p.id, p.producer_id FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "%s) "
			+ "AS prod ON pr.id = prod.producer_id "
			+ "GROUP BY pr.id, pr.name "
			+ "ORDER BY pr.name";
	
	@Override
	protected String getSqlTemplate() {
		return GET_PRODUCERS_BY_CRITERIA_TEMPLATE;
	}

}
