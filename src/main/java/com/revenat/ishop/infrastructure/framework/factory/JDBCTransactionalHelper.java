package com.revenat.ishop.infrastructure.framework.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
		
		boolean isNestedTransaction = JDBCConnectionFactory.isCurrentConnectionExists();
		Connection conn = null;
		try /*(Connection conn = dataSource.getConnection())*/ {
			conn = obtainCurrentConnection(isNestedTransaction);

			if (transactional.readOnly()) {
				return method.invoke(realService, args);
			} else {
				return invokeInsideTransaction(conn, method, args, isNestedTransaction);
			}

		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		} finally {
			discardCurrentConnection(conn, isNestedTransaction);
		}
	}

	private Object invokeInsideTransaction(Connection conn, Method method, Object[] args, boolean isNestedTransaction)
			throws SQLException, IllegalAccessException, InvocationTargetException {
		try {
			if (!isNestedTransaction) {
				TransactionSynchronizationManager.initSynchronization();
			}
			Object result = method.invoke(realService, args);
			if (!isNestedTransaction) {
				conn.commit();
				afterCommit();
			}
			return result;
		} catch (SQLException | InvocationTargetException | IllegalAccessException | FrameworkPersistenceException e) {
			conn.rollback();
			throw e;
		} catch (RuntimeException e) {
			if (hasSqlExceptionCause(e)) {
				conn.rollback();
			} else {
				if (!isNestedTransaction) {
					conn.commit();
				}
			}
			throw e;
		} finally {
			if (!isNestedTransaction) {
				TransactionSynchronizationManager.clearSynchronization();
			}
		}
	}

	private void afterCommit() {
		List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
		for (TransactionSynchronization synchronization : synchronizations) {
			synchronization.afterCommit();
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
	
	private Connection obtainCurrentConnection(boolean isNestedTransaction) throws SQLException {
		Connection conn = isNestedTransaction ? JDBCConnectionFactory.getCurrentConnection() : dataSource.getConnection();
		if (!isNestedTransaction) {
			JDBCConnectionFactory.setCurrentConnection(conn);
		}
		return conn;
	}
	
	private void discardCurrentConnection(Connection conn, boolean isNestedTransaction) {
		if (!isNestedTransaction) {
			handleCloseConnection(conn);
			JDBCConnectionFactory.removeCurrentConnection();
		}
	}

	private static void handleCloseConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		}
	}
}
