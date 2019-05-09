package com.revenat.ishop.repository.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.revenat.ishop.exception.DataStorageException;
import com.revenat.ishop.search.criteria.ProductCriteria;

/**
 * This is the base parent class for all JDBC repositories.
 * 
 * @author Vitaly Dragun
 *
 */
abstract class AbstractJdbcRepository {
	private final DataSource dataSource;

	protected AbstractJdbcRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected SqlQuery buildSqlQuery(ProductCriteria criteria, String sqlTemplate, Object... additionalParameters) {
		String sqlQuery = buildSqlQuery(sqlTemplate, criteria);
		Object[] parameters = buildParamsArray(criteria, additionalParameters);
		return new SqlQuery(sqlQuery, parameters);
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

	protected <T> T executeSelect(Function<T> func) {
		try (Connection con = dataSource.getConnection()) {
			return func.execute(con);
		} catch (SQLException e) {
			throw new DataStorageException("Error while executing sql query", e);
		}
	}

	protected <T> T executeUpdate(Function<T> func) {
		try (Connection con = dataSource.getConnection()) {
			T result = func.execute(con);
			con.commit();
			return result;
		} catch (SQLException e) {
			throw new DataStorageException("Error while executing sql query", e);
		}
	}

	protected static class SqlQuery {
		private final String query;
		private final Object[] parameters;

		public SqlQuery(String query, Object[] parameters) {
			this.query = query;
			this.parameters = parameters;
		}

		public String getQuery() {
			return query;
		}

		public Object[] getParameters() {
			return parameters;
		}
	}

	@FunctionalInterface
	protected interface Function<T> {
		T execute(Connection connection) throws SQLException;
	}
}
