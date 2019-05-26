package com.revenat.ishop.infrastructure.repository.jdbc.plain;

import javax.sql.DataSource;

import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.util.JDBCUtils;
import com.revenat.ishop.infrastructure.util.JDBCUtils.ResultSetHandler;
import com.revenat.ishop.infrastructure.repository.AccountRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.base.AbstractJdbcRepository;

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
			ResultSetHandlerFactory.getIdResultSetHandler("id");
	private static final ResultSetHandler<Account> ACCOUNT_HANDLER =
			ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ACCOUNT_RESULT_SET_HANDLER);

	public JdbcAccountRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Account findByEmail(String email) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ACCOUNT_BY_EMAIL, ACCOUNT_HANDLER, email));
	}
	
	@Override
	public Account save(Account account) {
		Integer id = executeUpdate(conn -> 
			JDBCUtils.insert(conn, SqlQueries.INSERT_ACCOUNT, GENERATED_ID_HANDLER, account.getName(), account.getEmail(), account.getAvatarUrl())
		);
		account.setId(id);
		return account;
	}
}
