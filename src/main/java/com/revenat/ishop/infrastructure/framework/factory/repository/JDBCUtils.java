package com.revenat.ishop.infrastructure.framework.factory.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.revenat.ishop.infrastructure.framework.exception.FrameworkPersistenceException;
import com.revenat.ishop.infrastructure.framework.handler.ResultSetHandler;
import com.revenat.ishop.infrastructure.util.Checks;

/**
 * Contains helper methods to work with JDBC.
 * 
 * @author Vitaly Dragun
 *
 */
public final class JDBCUtils {

	private JDBCUtils() {
	}

	/**
	 * Creates and executes SELECT SQL statement, using specified parameters.
	 * 
	 * @param conn             active connection to the database
	 * @param sql              SQL query to execute against the database
	 * @param resultSetHandler implementation of the {@link ResultSetHandler} with
	 *                         custom logic to handle returned result set
	 * @param params           optional parameters to be passed into generated SQL
	 *                         query
	 * @return custom type that represents result of handling result set returned
	 *         from executed SELECT statement
	 * @throws NullPointerException          if any of the arguments except
	 *                                       {@code params} is null.
	 * @throws FrameworkPersistenceException if some sort of error occurs during
	 *                                       work with the database.
	 */
	static <T> T select(Connection conn, String sql, ResultSetHandler<T> resultSetHandler, Object... params) {
		checkParams(conn, sql, resultSetHandler);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			populatePreparedStatement(ps, params);
			try (ResultSet rs = ps.executeQuery()) {
				return resultSetHandler.handle(rs);
			}
		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		}
	}

	/**
	 * Creates and executes INSERT SQL statement, using specified parameters.
	 * 
	 * @param conn             active connection to the database
	 * @param sql              SQL INSERT query to execute against the database
	 * @param resultSetHandler implementation of the {@link ResultSetHandler} with
	 *                         custom logic to handle returned result set with
	 *                         auto-generated keys after success insert
	 * @param params           optional parameters to be passed into generated SQL
	 *                         query
	 * @return custom type that represents result of handling result set returned
	 *         from executed INSERT statement (auto-generated keys)
	 * @throws NullPointerException          if any of the arguments except
	 *                                       {@code params} is null.
	 * @throws FrameworkPersistenceException if some sort of error occurs during
	 *                                       work with the database.
	 */
	static <T> T insert(Connection conn, String sql, ResultSetHandler<T> resultSetHandler, Object... params) {
		checkParams(conn, sql, resultSetHandler);

		try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			populatePreparedStatement(ps, params);
			int result = ps.executeUpdate();
			if (result != 1) {
				throw new SQLException("Can not insert new row to the database. Result=" + result);
			}
			try (ResultSet rs = ps.getGeneratedKeys()) {
				return resultSetHandler.handle(rs);
			}
		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		}
	}

	/**
	 * Creates and executes number of SQL statements in one single batch operation.
	 * 
	 * @param conn           active connection to the database
	 * @param sql            SQL INSERT query to execute against the database
	 * @param parametersList list with parameters for each insert command in the
	 *                       batch
	 * @throws FrameworkPersistenceException if some sort of error occurs during
	 *                                       work with the database.
	 */
	static int insertBatch(Connection conn, String sql, List<Object[]> parametersList) {
		checkParams(conn, sql, parametersList);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (Object[] parameters : parametersList) {
				populatePreparedStatement(ps, parameters);
				ps.addBatch();
			}
			int[] updateCounts = ps.executeBatch();
			int rowsInserted = Arrays.stream(updateCounts).sum();
			if (rowsInserted != parametersList.size()) {
				throw new SQLException(
						String.format("Can not insert all rows to the database: rows to insert: %d, inserted: %d",
								parametersList.size(), rowsInserted));
			}
			return rowsInserted;
		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		}
	}

	/**
	 * Creates and executes UPDATE/DELETE SQL statement, using specified parameters.
	 * 
	 * @param conn   active connection to the database
	 * @param sql    SQL query to execute against the database
	 * @param params optional parameters to be inserted into generated SQL query
	 * @return number of updated rows
	 * @throws NullPointerException          if any of the arguments except
	 *                                       {@code params} is null.
	 * @throws FrameworkPersistenceException if some sort of error occurs during
	 *                                       work with the database.
	 */
	static int executeUpdate(Connection conn, String sql, Object... params) {
		checkParams(conn, sql);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			populatePreparedStatement(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new FrameworkPersistenceException(e);
		}
	}

	private static void populatePreparedStatement(PreparedStatement ps, Object... params) throws SQLException {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
		}
	}

	private static void checkParams(Connection conn, String sql, ResultSetHandler<?>... handlers) {
		Checks.checkParam(conn != null, "Connection can not be null");
		Checks.checkParam(sql != null, "Sql string can not be null");
		for (ResultSetHandler<?> handler : handlers) {
			Checks.checkParam(handler != null, "Result set handler can not be null");
		}
	}

	private static void checkParams(Connection conn, String sql, List<Object[]> parameterssList) {
		checkParams(conn, sql);
		Checks.checkParam(parameterssList != null && !parameterssList.isEmpty(),
				"Can not execute insertBatch with null or empty parameter list");
	}
}
