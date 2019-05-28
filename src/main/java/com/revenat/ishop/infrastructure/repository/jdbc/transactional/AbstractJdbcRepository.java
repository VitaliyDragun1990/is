package com.revenat.ishop.infrastructure.repository.jdbc.transactional;

import java.sql.Connection;

import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkPersistenceException;
import com.revenat.ishop.infrastructure.framework.factory.transaction.JDBCConnectionFactory;
import com.revenat.ishop.infrastructure.repository.jdbc.base.AbstractRepository;

/**
 * This is the base parent class for all JDBC repositories.
 * 
 * @author Vitaly Dragun
 *
 */
public abstract class AbstractJdbcRepository extends AbstractRepository {

	protected <T> T execute(Function<T> func) {
		try {
			Connection conn = JDBCConnectionFactory.getCurrentConnection();
			return func.execute(conn);
		} catch (FrameworkPersistenceException e) {
			Throwable cause = e.getCause();
			throw new PersistenceException("Error while executing sql query: " + cause.getMessage(), cause);
		}
	}

	@FunctionalInterface
	protected interface Function<T> {
		T execute(Connection connection);
	}
}
