package com.revenat.ishop.infrastructure.repository.jdbc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.repository.DataSourceFactory;
import com.revenat.ishop.infrastructure.repository.jdbc.framework.JdbcAccountRepository;

public class JdbcAccountRepositoryTest {
	
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	private BasicDataSource dataSource;
	private JdbcAccountRepository repository;
	
	@Before
	public void setUp() {
		dataSource = DataSourceFactory.createH2DataSource();
		repository = new JdbcAccountRepository(dataSource);
	}
	
	@After
	public void tearDown() throws SQLException {
		dataSource.close();
	}

	@Test
	public void shouldAllowToGetAccountByEmail() throws Exception {
		String email = "jack@test.com";
		Account account = repository.getByEmail(email);
		
		assertThat(account.getEmail(), equalTo(email));
	}
	
	@Test
	public void shouldReturnNullIfNoAccountWithGivenEmail() throws Exception {
		Account account = repository.getByEmail("unknown@test.com");
		
		assertNull(account);
	}
	
	@Test
	public void shouldAllowToSaveNewAccount() throws Exception {
		Account newAccount = new Account();
		newAccount.setName("Bill");
		String email = "bill@test.com";
		newAccount.setEmail(email);
		
		repository.save(newAccount);
		
		Account savedAccount = repository.getByEmail(email);
		assertNotNull(savedAccount);
	}
	
	@Test
	public void shouldNotAllowToSaveAccountIfEmailAlreadyOccupied() throws Exception {
		expected.expect(PersistenceException.class);
		String occupiedEmail = "jack@test.com";
		Account newAccount = new Account("jack smith", occupiedEmail);
		
		repository.save(newAccount);
	}

}
