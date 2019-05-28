package com.revenat.ishop.infrastructure.framework.sql.queries;

import java.lang.reflect.Field;
import java.util.List;

public class BatchInsertQuery {
	private final Field idField;
	private final String query;
	private final List<Object[]> parameters;

	public BatchInsertQuery(Field idField, String query, List<Object[]> parameters) {
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

	public List<Object[]> getParameters() {
		return parameters;
	}
}