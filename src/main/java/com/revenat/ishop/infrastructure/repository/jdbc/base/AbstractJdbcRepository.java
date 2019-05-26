package com.revenat.ishop.infrastructure.repository.jdbc.base;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkPersistenceException;

/**
 * This is the base parent class for all JDBC repositories.
 * 
 * @author Vitaly Dragun
 *
 */
public abstract class AbstractJdbcRepository extends AbstractRepository {
	private final DataSource dataSource;

	protected AbstractJdbcRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected <T> T executeSelect(Function<T> func) {
		try (Connection con = dataSource.getConnection()) {
			return func.execute(con);
		} catch (SQLException | FrameworkPersistenceException e) {
			throw new PersistenceException("Error while executing sql query", e);
		}
	}

	protected <T> T executeUpdate(Function<T> func) {
		Connection ref = null;
		try (Connection conn = dataSource.getConnection()) {
			ref = conn;
			T result =  func.execute(conn);
			conn.commit();
			return result;
		} catch (SQLException | FrameworkPersistenceException e) {
			rollback(ref, e);
			throw new PersistenceException("Error while executing sql query", e);
		}
	}

	private void rollback(Connection conn, Exception mainExc) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				mainExc.addSuppressed(e);
			}
		}
	}


	@FunctionalInterface
	protected interface Function<T> {
		T execute(Connection connection) throws SQLException;
	}
}
