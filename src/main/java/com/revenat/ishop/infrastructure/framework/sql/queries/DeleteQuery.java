package com.revenat.ishop.infrastructure.framework.sql.queries;

public class DeleteQuery {
	private final String query;
	private final Object[] parameters;

	public DeleteQuery( String query, Object[] parameters) {
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