package com.revenat.ishop.infrastructure.repository.jdbc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.repository.DataSourceFactory;
import com.revenat.ishop.infrastructure.repository.jdbc.framework.JdbcOrderItemRepository;

public class JdbcOrderItemRepositoryTest {
	
	private BasicDataSource dataSource;
	private JdbcOrderItemRepository repository;
	
	@Before
	public void setUp() {
		dataSource = DataSourceFactory.createH2DataSource();
		repository = new JdbcOrderItemRepository(dataSource);
	}
	
	@After
	public void tearDown() throws SQLException {
		dataSource.close();
	}

	@Test
	public void allowToGetOrderItemsForParticularOrder() throws Exception {
		long orderId = 1L;
		
		List<OrderItem> orderItems = repository.getByOrderId(orderId);
		
		assertThat(orderItems, hasSize(2));
		assertThat(orderItems, everyItem(hasProperty("orderId", equalTo(orderId))));
	}
	
	@Test
	public void shouldReturnEmptyListIfNoOrderItemsForParticularOrder() throws Exception {
		long orderId = 2L; // order id for order without order items
		
		List<OrderItem> orderItems = repository.getByOrderId(orderId);
		
		assertThat(orderItems, empty());
	}

	@Test
	public void shouldAllowToSaveNewOrderItem() throws Exception {
		long orderId = 1L;
		int productId = 3;
		int quantity = 10;
		OrderItem newItem = createItem(orderId, productId, quantity);
		
		repository.save(newItem);
		
		List<OrderItem> orderItems = repository.getByOrderId(orderId);
		assertThat(orderItems, hasItem(
				allOf(
						hasProperty("quantity", equalTo(quantity)),
						hasProperty("product", hasProperty("id", equalTo(productId)))
				)));
	}
	
	@Test
	public void shouldAllowToSaveSeveralOrderItemsAtOnce() throws Exception {
		long orderId = 2L;
		List<OrderItem> items = new ArrayList<>();
		items.add(createItem(orderId, 1, 1));
		items.add(createItem(orderId, 2, 2));
		
		repository.saveAll(items);
		
		List<OrderItem> orderItems = repository.getByOrderId(orderId);
		assertThat(orderItems, hasSize(2));
		assertThat(orderItems, hasItem(
				allOf(
						hasProperty("quantity", equalTo(1)),
						hasProperty("product", hasProperty("id", equalTo(1)))
				)));
		assertThat(orderItems, hasItem(
				allOf(
						hasProperty("quantity", equalTo(2)),
						hasProperty("product", hasProperty("id", equalTo(2)))
				)));
	}
	
	private OrderItem createItem(long orderId, int productId,  int quantity) {
		Product p = new Product();
		p.setId(productId);
		OrderItem newItem = new OrderItem();
		newItem.setOrderId(orderId);
		newItem.setProduct(p);
		newItem.setQuantity(quantity);
		
		return newItem;
	}
}
