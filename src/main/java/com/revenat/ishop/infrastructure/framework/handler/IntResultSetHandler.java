package com.revenat.ishop.infrastructure.framework.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.revenat.ishop.infrastructure.framework.util.JDBCUtils.ResultSetHandler;

public class IntResultSetHandler implements ResultSetHandler<Integer> {

	@Override
	public Integer handle(ResultSet rs) throws SQLException {
		if (rs.next()) {
			return rs.getInt(1);
		} else {
			return 0;
		}
	}

}
