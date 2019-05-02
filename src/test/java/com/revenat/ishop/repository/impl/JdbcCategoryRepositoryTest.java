package com.revenat.ishop.repository.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.entity.Category;
import com.revenat.ishop.repository.DataSourceFactory;

public class JdbcCategoryRepositoryTest {

	private BasicDataSource dataSource;
	private JdbcCategoryRepository repository;

	@Before
	public void setUp() {
		dataSource = DataSourceFactory.createH2DataSource();
		repository = new JdbcCategoryRepository(dataSource);
	}
	
	@After
	public void tearDown() throws SQLException {
		dataSource.close();
	}

	@Test
	public void shouldAllowToGetAllCategories() throws Exception {
		List<Category> categories = repository.getAll();
		
		assertThat(categories, hasSize(3));
	}

}
