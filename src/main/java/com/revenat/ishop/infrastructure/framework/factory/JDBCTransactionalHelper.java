package com.revenat.ishop.infrastructure.framework.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.framework.exception.FrameworkPersistenceException;

/**
 * This component is responsible for invoking specified {@code realService}
 * methods inside single transaction.
 * 
 * @author Vitaly Dragun
 *
 */
class JDBCTransactionalHelper {
	private final Object realService;
	private final DataSource dataSource;

	JDBCTransactionalHelper(Object realService, DataSource dataSource) {
		this.realService = realService;
		this.dataSource = dataSource;
	}

	Object invokeInsideTransaction(Transactional transactional, Method method, Object[] args)
			throws IllegalAccessException, InvocationTargetException {

		try (Connection conn = dataSource.getConnection()) {
			JDBCConnectionFactory.setCurrentConnection(conn);

			if (transactional.readOnly()) {
				return method.invoke(realService, args);
			} else {
				return invokeInsideTransaction(conn, method, args);
			}

		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		} finally {
			JDBCConnectionFactory.removeCurrentConnection();
		}
	}

	private Object invokeInsideTransaction(Connection conn, Method method, Object[] args)
			throws SQLException, IllegalAccessException, InvocationTargetException {
		try {
			Object result = method.invoke(realService, args);
			conn.commit();
			return result;
		} catch (SQLException | InvocationTargetException | IllegalAccessException | FrameworkPersistenceException e) {
			conn.rollback();
			throw e;
		} catch (RuntimeException e) {
			if (hasSqlExceptionCause(e)) {
				conn.rollback();
			} else {
				conn.commit();
			}
			throw e;
		} 
	}

	private boolean hasSqlExceptionCause(RuntimeException e) {
		Throwable cause = null;
		while ((cause = e.getCause()) != null) {
			if (cause instanceof SQLException) {
				return true;
			}
		}
		return false;
	}

}
