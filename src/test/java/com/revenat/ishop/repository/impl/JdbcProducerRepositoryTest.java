package com.revenat.ishop.repository.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.repository.DataSourceFactory;

public class JdbcProducerRepositoryTest {
	private BasicDataSource dataSource;
	private JdbcProducerRepository repository;

	@Before
	public void setUp() {
		dataSource = DataSourceFactory.createH2DataSource();
		repository = new JdbcProducerRepository(dataSource);
	}
	
	@After
	public void tearDown() throws SQLException {
		dataSource.close();
	}
	
	@Test
	public void shouldAllowToGetAllProducers() throws Exception {
		List<Producer> producers = repository.getAll();
		
		assertThat(producers, hasSize(4));
	}
}
