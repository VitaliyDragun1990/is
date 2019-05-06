package com.revenat.ishop.repository.impl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.entity.Order;
import com.revenat.ishop.entity.OrderItem;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.repository.DataSourceFactory;

public class JdbcOrderRepositoryTest {

	private BasicDataSource dataSource;
	private JdbcOrderRepository repository;
	private JdbcOrderItemRepository itemRepo;

	@Before
	public void setUp() {
		dataSource = DataSourceFactory.createH2DataSource();
		itemRepo = new JdbcOrderItemRepository(dataSource);
		repository = new JdbcOrderRepository(dataSource, itemRepo);
	}

	@After
	public void tearDown() throws SQLException {
		dataSource.close();
	}

	@Test
	public void shouldAllowToGetOrderById() throws Exception {
		long orderId = 1L;
		Order order = repository.getById(orderId);

		assertThat(order.getId(), equalTo(orderId));
	}

	@Test
	public void shouldReturnNullIfNoOrderWithSuchId() throws Exception {
		Order order = repository.getById(999L);

		assertNull(order);
	}

	@Test
	public void shouldReturnOrderWithAllOrderItems() throws Exception {
		long orderId = 1L;
		Order order = repository.getById(orderId);

		assertThat(order.getItems(), hasSize(2));
		assertThat(order.getItems(),
				everyItem(allOf(hasProperty("id", notNullValue()), hasProperty("orderId", notNullValue()),
						hasProperty("product", notNullValue()), hasProperty("quantity", greaterThan(0)))));
	}

	@Test
	public void shouldAllowToSaveNewOrder() throws Exception {
		Order newOrder = new Order();
		newOrder.setAccountId(2);
		newOrder.addItem(createItem(1, 10));
		newOrder.addItem(createItem(2, 5));

		repository.save(newOrder);

		Order createdOrder = repository.getById(newOrder.getId());
		assertThat(createdOrder.getItems(), hasSize(2));
		assertThat(createdOrder.getItems(),
				hasItem(allOf(hasProperty("id", notNullValue()), hasProperty("orderId", equalTo(createdOrder.getId())),
						hasProperty("product", hasProperty("id", equalTo(1))), hasProperty("quantity", equalTo(10)))));
		assertThat(createdOrder.getItems(),
				hasItem(allOf(hasProperty("id", notNullValue()), hasProperty("orderId", equalTo(createdOrder.getId())),
						hasProperty("product", hasProperty("id", equalTo(2))), hasProperty("quantity", equalTo(5)))));
	}

	@Test
	public void shouldAllowToGetOrdersByAccountId() throws Exception {
		int accountId = 1;

		List<Order> orders = repository.getByAccountId(accountId, 0, 5);

		assertThat(orders, hasSize(5));
	}

	@Test
	public void shouldReturnEmptyListIfNoOrdersForSuchAccountId() throws Exception {
		int accountId = 999;

		List<Order> orders = repository.getByAccountId(accountId, 0, 5);

		assertThat(orders, empty());
	}

	@Test
	public void shouldReturnOrdersInDescendingChronologicalOrder() throws Exception {
		int accountId = 1;

		List<Order> orders = repository.getByAccountId(accountId, 0, 5);

		assertThat(orders.get(0).getCreated(), LocalDateTimeMatchers.after(orders.get(1).getCreated()));
		assertThat(orders.get(1).getCreated(), LocalDateTimeMatchers.after(orders.get(2).getCreated()));
		assertThat(orders.get(2).getCreated(), LocalDateTimeMatchers.after(orders.get(3).getCreated()));
		assertThat(orders.get(3).getCreated(), LocalDateTimeMatchers.after(orders.get(4).getCreated()));
	}
	
	@Test
	public void shouldAllowToCountOrdersByAccountId() throws Exception {
		int accountId = 1;
		
		int count = repository.countByAccountId(accountId);
		
		assertThat(count, equalTo(6));
	}
	
	@Test
	public void shouldReturnZeroIfNoOrdersForSpecifiedAccountId() throws Exception {
		int accountId = 2;
		
		int count = repository.countByAccountId(accountId);
		
		assertThat(count, equalTo(0));
	}

	private OrderItem createItem(int productId, int quantity) {
		Product p = new Product();
		p.setId(productId);
		OrderItem newItem = new OrderItem();
		newItem.setProduct(p);
		newItem.setQuantity(quantity);

		return newItem;
	}
}
