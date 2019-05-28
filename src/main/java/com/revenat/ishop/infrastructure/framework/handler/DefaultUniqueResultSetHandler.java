package com.revenat.ishop.infrastructure.framework.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.revenat.ishop.infrastructure.framework.handler.base.DefaultResultSetHandler;

public class DefaultUniqueResultSetHandler<T> implements ResultSetHandler<T> {
	private ResultSetHandler<T> oneRowResultSetHandler;
	
	public DefaultUniqueResultSetHandler(ResultSetHandler<T> oneRowResultSetHandler) {
		this.oneRowResultSetHandler = oneRowResultSetHandler;
	}

	public DefaultUniqueResultSetHandler(Class<T> entityClass) {
		this(new DefaultResultSetHandler<>(entityClass));
	}

	@Override
	public T handle(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return oneRowResultSetHandler.handle(rs);
		} else {
			return null;
		}
	}

}
