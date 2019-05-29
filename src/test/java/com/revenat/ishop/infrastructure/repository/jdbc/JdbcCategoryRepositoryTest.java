package com.revenat.ishop.infrastructure.repository.jdbc;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.DataSourceFactory;
import com.revenat.ishop.infrastructure.repository.jdbc.plain.JdbcCategoryRepository;

public class JdbcCategoryRepositoryTest {
	private static final int CATEGORY_TV_ID = 3;
	private static final int CATEGORY_SMARTPHONE_ID = 2;
	private static final int CATEGORY_TABLET_ID = 1;
	private static final int SONY_PRODUCER_ID = 3;
	private static final int APPLE_PRODUCER_ID = 1;
	
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
		List<Category> categories = repository.findAll();
		
		assertThat(categories, hasSize(3));
	}
	
	@Test
	public void shouldAllowToGetCategoriesByCriteria() throws Exception {
		ProductCriteria criteria = ProductCriteria.byCategoriesAndProducers("tab", Collections.emptyList(),
				Arrays.asList(APPLE_PRODUCER_ID, SONY_PRODUCER_ID));
		
		List<Category> categories = repository.findByCriteria(criteria);
		
		assertThat(categories, hasSize(3));
		assertThat(categories, hasItem(
				allOf(
				hasProperty("id", equalTo(CATEGORY_TABLET_ID)),
				hasProperty("productCount", equalTo(1))))
				);
		assertThat(categories, hasItem(
				allOf(
				hasProperty("id", equalTo(CATEGORY_SMARTPHONE_ID)),
				hasProperty("productCount", equalTo(0))))
				);
		assertThat(categories, hasItem(allOf(
				hasProperty("id", equalTo(CATEGORY_TV_ID)),
				hasProperty("productCount", equalTo(0))))
				);
	}

}
