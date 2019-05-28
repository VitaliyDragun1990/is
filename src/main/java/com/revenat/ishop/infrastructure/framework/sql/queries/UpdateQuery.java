package com.revenat.ishop.infrastructure.framework.sql.queries;

public class UpdateQuery {
	private final String query;
	private final Object[] parameters;

	public UpdateQuery( String query, Object[] parameters) {
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