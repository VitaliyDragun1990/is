package com.revenat.ishop.infrastructure.repository.jdbc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.DataSourceFactory;
import com.revenat.ishop.infrastructure.repository.jdbc.JdbcProducerRepository;

public class JdbcProducerRepositoryTest {
	private static final int TABLET_CATEGORY_ID = 1;
	private static final int SMARTPHONE_CATEGORY_ID = 2;
	private static final int PRODUCER_DELL_ID = 4;
	private static final int PRODUCER_SONY_ID = 3;
	private static final int PRODUCER_SAMSUNG_ID = 2;
	private static final int PRODUCER_APPLE_ID = 1;
	
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
	
	@Test
	public void shouldAllowToGetProducersByCriteria() throws Exception {
		ProductCriteria criteria = ProductCriteria.byCategories("desc", Arrays.asList(TABLET_CATEGORY_ID, SMARTPHONE_CATEGORY_ID));
		
		List<Producer> producers = repository.getByCriteria(criteria);
		
		assertThat(producers, hasSize(4));
		assertThat(producers, hasItem(
				allOf(
				hasProperty("id", equalTo(PRODUCER_APPLE_ID)),
				hasProperty("productCount", equalTo(2))))
				);
		assertThat(producers, hasItem(
				allOf(
				hasProperty("id", equalTo(PRODUCER_SAMSUNG_ID)),
				hasProperty("productCount", equalTo(1))))
				);
		assertThat(producers, hasItem(allOf(
				hasProperty("id", equalTo(PRODUCER_SONY_ID)),
				hasProperty("productCount", equalTo(0))))
				);
		assertThat(producers, hasItem(allOf(
				hasProperty("id", equalTo(PRODUCER_DELL_ID)),
				hasProperty("productCount", equalTo(1))))
				);
	}
}
