package com.revenat.ishop.infrastructure.framework.factory;

import java.sql.Connection;

import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;

/**
 * This component is responsible for providing distinct {@link Connection}
 * object unique per thread of execution.
 * 
 * @author Vitaly Dragun
 *
 */
final class JDBCConnectionFactory {
	private static final ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Returns {@link Connection} associated with given thread of execution
	 * 
	 * @throws {@link FrameworkSystemException} if no connection associated with
	 *         current thread of execution.
	 */
	public static Connection getCurrentConnection() {
		Connection conn = connections.get();
		if (conn == null) {
			throw new FrameworkSystemException("Connection not found for current thread. "
					+ "Does your repository have @Transactional annotation?");
		}
		return conn;
	}

	static void setCurrentConnection(Connection conn) {
		connections.set(conn);
	}

	static void removeCurrentConnection() {
		connections.remove();
	}
	
	static boolean isCurrentConnectionExists() {
		return connections.get() != null;
	}

	private JDBCConnectionFactory() {
	}
}
