package com.revenat.ishop.domain.entity;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.exparity.hamcrest.date.LocalDateTimeMatchers;
import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.Product;

public class OrderTest {
	
	private Order order;
	
	@Before
	public void setUp() {
		order = new Order();
	}

	@Test
	public void newlyCreatedOrderShouldBeEmpty() throws Exception {
		assertThat(order.getItems(), empty());
	}
	
	@Test
	public void newlyCreatedOrderShouldHaveTotalCostOfZero() throws Exception {
		assertThat(order.getTotalCost(), equalTo(BigDecimal.ZERO));
	}
	
	@Test
	public void newlyCreatedOrderShouldHaveCreatedDate() throws Exception {
		assertThat(order.getCreated(), LocalDateTimeMatchers.sameOrBefore(LocalDateTime.now()));
	}
	
	@Test
	public void shouldAllowToAddItemsToOrder() throws Exception {
		Product p = createProduct(1, BigDecimal.valueOf(10.0));
		int quantity = 1;
		
		order.addItem(p, quantity);
		
		assertThat(order.getItems(), hasSize(1));
	}
	
	@Test
	public void shouldIncrementOrderTotalCostWhenAddItemsToOrder() throws Exception {
		Product productA = createProduct(1, BigDecimal.valueOf(10.0));
		Product productB = createProduct(2, BigDecimal.valueOf(20.0));
		
		order.addItem(productA, 1);
		assertThat(order.getTotalCost(), equalTo(BigDecimal.valueOf(10.0)));
		
		order.addItem(productB, 2);
		assertThat(order.getTotalCost(), equalTo(BigDecimal.valueOf(50.0)));
	}

	private Product createProduct(int id, BigDecimal price) {
		Product p = new Product();
		p.setId(id);
		p.setPrice(price);
		return p;
	}

}
