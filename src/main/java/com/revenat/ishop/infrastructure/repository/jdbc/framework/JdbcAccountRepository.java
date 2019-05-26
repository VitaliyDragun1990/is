package com.revenat.ishop.infrastructure.repository.jdbc.framework;

import javax.sql.DataSource;

import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.framework.handler.DefaultUniqueResultSetHandler;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils.ResultSetHandler;
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
	private static final String GET_ACCOUNT_BY_EMAIL = "SELECT * FROM account WHERE email = ?";
	private static final String INSERT_ACCOUNT = "INSERT INTO account (name, email, avatar_url) VALUES (?,?,?)";
	private static final ResultSetHandler<Account> ACCOUNT_HANDLER = new DefaultUniqueResultSetHandler<>(Account.class);

	public JdbcAccountRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public Account findByEmail(String email) {
		return executeSelect(conn -> FrameworkJDBCUtils.select(conn, GET_ACCOUNT_BY_EMAIL, ACCOUNT_HANDLER, email));
	}
	
	@Override
	public Account save(Account account) {
		Account saved = executeUpdate(conn -> 
			FrameworkJDBCUtils.insert(conn, INSERT_ACCOUNT, ACCOUNT_HANDLER, account.getName(), account.getEmail(), account.getAvatarUrl())
		);
		account.setId(saved.getId());
		return account;
	}
}
