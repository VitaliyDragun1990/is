package com.revenat.ishop.infrastructure.repository.jdbc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.DataSourceFactory;
import com.revenat.ishop.infrastructure.repository.jdbc.framework.JdbcProductRepository;

public class JdbcProductRepositoryTest {
	private static final int TABLET_CATEGORY_ID = 1;
	private static final int SMARTPHONE_CATEGORY_ID = 2;
	private static final int TV_CATEGORY_ID = 3;
	private static final int APPLE_PRODUCER_ID = 1;
	private static final int SAMSUNG_PRODUCER_ID = 2;
	private static final int SONY_PRODUCER_ID = 3;

	private BasicDataSource dataSource;
	private JdbcProductRepository repository;

	@Before
	public void setUp() {
		dataSource = DataSourceFactory.createH2DataSource();
		repository = new JdbcProductRepository(dataSource);
	}

	@After
	public void tearDown() throws SQLException {
		dataSource.close();
	}

	@Test
	public void shouldAllowToGetAllProductsAccordingToOffsetAndLimit() throws SQLException {
		List<Product> products = repository.getAll(0, 10);

		assertThat(products, hasSize(4));
	}

	@Test
	public void shouldReturnEmptyListIfNoProductsForGivenOffset() throws Exception {
		List<Product> products = repository.getAll(10, 10);

		assertThat(products, empty());
	}

	@Test
	public void shouldAllowToGetProductWithGivenId() throws Exception {
		Product product = repository.getById(1);

		assertThat(product.getId(), equalTo(1));
	}

	@Test
	public void shouldReturnNullIfThereIsNoProductWithGivenId() throws Exception {
		Product product = repository.getById(100);

		assertNull(product);
	}

	@Test
	public void shouldAllowToCountAllProductsInTheDatabase() throws Exception {
		int productCount = repository.countAll();

		assertThat(productCount, equalTo(4));
	}

	@Test
	public void shouldAllowToGetProductsByCategory() throws Exception {
		List<Product> products = repository.getByCategory("/tablet", 0, 10);

		assertThat(products, hasSize(2));
		assertThat(products, everyItem(hasProperty("category", equalToIgnoringCase("tablet"))));
	}

	@Test
	public void shouldAllowToCountProductsByCategory() throws Exception {
		int countByCategory = repository.countByCategory("/tablet");

		assertThat(countByCategory, equalTo(2));
	}

	@Test
	public void shouldReturnCountOfZeroIfThereAreNoProductsForSuchCategory() throws Exception {
		int countByCategory = repository.countByCategory("/tv");
		
		assertThat(countByCategory, is(0));
	}

	@Test
	public void shouldReturnEmptyListIfThereIsNoProductBySuchCategory() throws Exception {
		List<Product> products = repository.getByCategory("/tv", 0, 10);

		assertThat(products, empty());
	}
	
	@Test
	public void shouldAllowToFilterProductsByCriteria() throws Exception {
		String query = "tab";
		ProductCriteria criteria = ProductCriteria.byCategoriesAndProducers(
				query,
				Arrays.asList(TABLET_CATEGORY_ID),
				Arrays.asList(APPLE_PRODUCER_ID, SAMSUNG_PRODUCER_ID));
		
		List<Product> products = repository.getByCriteria(criteria, 0, 10);
		
		assertThat(products, hasSize(2));
		assertThat(products, everyItem(hasProperty("name", containsStringIgnoringCase(query))));
		assertThat(products, everyItem(hasProperty("category", equalToIgnoringCase("tablet"))));
		assertThat(products, hasItem(hasProperty("producer", equalToIgnoringCase("apple"))));
		assertThat(products, hasItem(hasProperty("producer", equalToIgnoringCase("samsung"))));
	}
	
	@Test
	public void shouldReturnAllProductsBelongingToSpecifiedCategoryIfQueryAndProducerCriteriaNotSet() throws Exception {
		ProductCriteria criteria = ProductCriteria.byCategories(
				"",
				Arrays.asList(TABLET_CATEGORY_ID, SMARTPHONE_CATEGORY_ID));
		
		List<Product> products = repository.getByCriteria(criteria, 0, 10);
		
		assertThat(products, hasSize(4));
		assertThat(products, hasItem(hasProperty("category", equalToIgnoringCase("tablet"))));
		assertThat(products, hasItem(hasProperty("category", equalToIgnoringCase("smartphone"))));
	}
	
	@Test
	public void shouldReturnAllProductsBelongingToSpecifiedProducerIfQueryAndCategoryCriteriaNotSet() throws Exception {
		ProductCriteria criteria = ProductCriteria.byProducers(
				"",
				Arrays.asList(APPLE_PRODUCER_ID, SAMSUNG_PRODUCER_ID));
				
		
		List<Product> products = repository.getByCriteria(criteria, 0, 10);
		
		assertThat(products, hasSize(3));
		assertThat(products, hasItem(hasProperty("producer", equalToIgnoringCase("apple"))));
		assertThat(products, hasItem(hasProperty("producer", equalToIgnoringCase("samsung"))));
	}
	
	@Test
	public void shouldReturnAllProductsSatisfyingSearchQueryIfProducerAndCategoryCriteriaNotSet() throws Exception {
		String query = "desc";
		ProductCriteria criteria = ProductCriteria.byCategoriesAndProducers(
				query,
				Collections.emptyList(),
				Collections.emptyList());
				
		
		List<Product> products = repository.getByCriteria(criteria, 0, 10);
		
		assertThat(products, hasSize(4));
		assertThat(products, hasItem(hasProperty("description", containsStringIgnoringCase(query))));
		assertThat(products, hasItem(hasProperty("description", containsStringIgnoringCase(query))));
	}
	
	@Test
	public void shouldReturnAllProductIfSearchCriteriaIsEmpty() throws Exception {
		ProductCriteria criteria = ProductCriteria.byCategoriesAndProducers(
				"",
				Collections.emptyList(),
				Collections.emptyList());
		
		List<Product> products = repository.getByCriteria(criteria, 0, 10);
		
		assertThat(products, hasSize(4));
	}
	
	@Test
	public void shouldReturnNoProductsIfThereAreNoProductsMatchSearchCriteria() throws Exception {
		ProductCriteria criteria = ProductCriteria.byProducers(
				"",
				Arrays.asList(SONY_PRODUCER_ID));
		
		List<Product> products = repository.getByCriteria(criteria, 0, 10);
		
		assertThat(products, empty());
	}
	
	@Test
	public void shouldAllowToCuntProductsByCriteria() throws Exception {
		String query = "tab";
		ProductCriteria criteria = ProductCriteria.byCategoriesAndProducers(
				query,
				Arrays.asList(TABLET_CATEGORY_ID),
				Arrays.asList(APPLE_PRODUCER_ID, SAMSUNG_PRODUCER_ID));
		
		int count = repository.countByCriteria(criteria);
		
		assertThat(count, equalTo(2));
	}
	
	@Test
	public void shouldReturnZeroIfThereAreNoProductsMatchSearchCriteria() throws Exception {
		ProductCriteria criteria = ProductCriteria.byCategoriesAndProducers(
				"",
				Arrays.asList(TV_CATEGORY_ID),
				Arrays.asList(SONY_PRODUCER_ID));
		
		int count = repository.countByCriteria(criteria);
		
		assertThat(count, equalTo(0));
	}

}
