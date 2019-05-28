package com.revenat.ishop.infrastructure.repository.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.sql.SQLBuilder;
import com.revenat.ishop.infrastructure.framework.sql.queries.SelectQuery;

abstract class AbstractProductCriteriaSQLBuilder implements SQLBuilder {

	@Override
	public SelectQuery buildSelectQuery(Object... builderParams) {
		ProductCriteria criteria = (ProductCriteria) builderParams[0];
		Object[] additionalParams = getAdditionalParams(builderParams);
		String sqlTemplate = getSqlTemplate();
		return buildSqlQuery(criteria, sqlTemplate, additionalParams);
	}

	private Object[] getAdditionalParams(Object[] builderParams) {
		if (builderParams.length > 1) {
			return Arrays.copyOfRange(builderParams, 1, builderParams.length);
		} else {
			return new Object[0];
		}
	}

	protected abstract String getSqlTemplate();

	protected SelectQuery buildSqlQuery(ProductCriteria criteria, String sqlTemplate, Object... additionalParameters) {
		String sqlQuery = buildSqlQuery(sqlTemplate, criteria);
		Object[] parameters = buildParamsArray(criteria, additionalParameters);
		return new SelectQuery(sqlQuery, parameters);
	}

	private String buildSqlQuery(String queryTemplate, ProductCriteria criteria) {
		List<Integer> categories = criteria.getCategoryIds();
		List<Integer> producers = criteria.getProducerIds();
		String whereClause = "";
		String byProductNameOrDescriptionClause = "";
		String byCategoriesClause = "";
		String byProducersClause = "";

		if (!criteria.getQuery().trim().isEmpty()) {
			byProductNameOrDescriptionClause = "(p.name ILIKE ? OR p.description ILIKE ?)";
		}
		if (!categories.isEmpty()) {
			byCategoriesClause = "c.id IN (" + createPlaceholders(categories.size()) + ")";
		}
		if (!producers.isEmpty()) {
			byProducersClause = "pr.id IN (" + createPlaceholders(producers.size()) + ")";
		}

		if (!byProductNameOrDescriptionClause.isEmpty() || !byCategoriesClause.isEmpty()
				|| !byProducersClause.isEmpty()) {
			whereClause = "WHERE";
			whereClause = appendClauseIfExists(whereClause, byProductNameOrDescriptionClause);
			whereClause = appendClauseIfExists(whereClause, byCategoriesClause);
			whereClause = appendClauseIfExists(whereClause, byProducersClause);
			whereClause = finalizeWhereClause(whereClause);
		}

		return String.format(queryTemplate, whereClause);
	}

	private String appendClauseIfExists(String whereClause, String filterClause) {
		if (!filterClause.isEmpty()) {
			return whereClause + " " + filterClause + " AND";
		}
		return whereClause;
	}

	private String finalizeWhereClause(String whereClause) {
		int substringIndex = whereClause.length() - " AND".length();
		return whereClause.substring(0, substringIndex);
	}

	private Object[] buildParamsArray(ProductCriteria criteria, Object... additionalParams) {
		List<Object> params = new ArrayList<>();
		String query = criteria.getQuery();
		List<Integer> categories = criteria.getCategoryIds();
		List<Integer> producers = criteria.getProducerIds();

		if (!query.trim().isEmpty()) {
			String queryParam = "%" + query + "%";
			params.add(queryParam);
			params.add(queryParam);
		}
		params.addAll(categories);
		params.addAll(producers);
		for (Object param : additionalParams) {
			params.add(param);
		}

		return params.toArray();
	}

	private String createPlaceholders(int count) {
		StringBuilder placeholders = new StringBuilder();

		for (int i = 0; i < count; i++) {
			placeholders.append("?,");
		}

		return placeholders.substring(0, placeholders.length() - 1);
	}
}
