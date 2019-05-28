package com.revenat.ishop.infrastructure.framework.sql.queries;

import java.lang.reflect.Field;

public class InsertQuery {
	private final Field idField;
	private final String query;
	private final Object[] parameters;

	public InsertQuery(Field idField, String query, Object[] parameters) {
		this.idField = idField;
		this.query = query;
		this.parameters = parameters;
	}

	public Field getIdField() {
		return idField;
	}

	public String getQuery() {
		return query;
	}

	public Object[] getParameters() {
		return parameters;
	}
}