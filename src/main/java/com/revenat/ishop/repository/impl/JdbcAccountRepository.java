package com.revenat.ishop.repository.impl;

import javax.sql.DataSource;

import com.revenat.ishop.entity.Account;
import com.revenat.ishop.repository.AccountRepository;
import com.revenat.ishop.util.jdbc.JDBCUtils;
import com.revenat.ishop.util.jdbc.JDBCUtils.ResultSetHandler;

/**
 * This is implementation of the {@link AccountRepository} that is responsible
 * for performing CRUD operations on {@link Account} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcAccountRepository extends AbstractJdbcRepository implements AccountRepository {
	private static final ResultSetHandler<Integer> GENERATED_ID_HANDLER =
			ResultSetHandlerFactory.GENERATED_INT_ID_RESULT_SET_HANDLER;
	private static final ResultSetHandler<Account> ACCOUNT_HANDLER =
			ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ACCOUNT_RESULT_SET_HANDLER);

	public JdbcAccountRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Account getByEmail(String email) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ACCOUNT_BY_EMAIL, ACCOUNT_HANDLER, email));
	}
	
	@Override
	public void save(Account account) {
		Integer id = executeUpdate(conn -> 
			JDBCUtils.insert(conn, SqlQueries.INSERT_ACCOUNT, GENERATED_ID_HANDLER, account.getName(), account.getEmail())
		);
		account.setId(id);
	}
}
