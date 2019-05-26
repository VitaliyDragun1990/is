package com.revenat.ishop.infrastructure.repository;

import javax.sql.DataSource;

import com.revenat.ishop.infrastructure.repository.jdbc.transactional.JdbcAccountRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.transactional.JdbcCategoryRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.transactional.JdbcOrderItemRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.transactional.JdbcOrderRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.transactional.JdbcProducerRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.transactional.JdbcProductRepository;

public final class RepositoryFactory {

	public static AccountRepository createAccountRepository(DataSource dataSource) {
		return new JdbcAccountRepository(/*dataSource*/);
	}
	
	public static CategoryRepository createCategoryRepository(DataSource dataSource) {
		return new JdbcCategoryRepository(/*dataSource*/);
	}
	
	public static OrderItemRepository createOrderItemRepository(DataSource dataSource) {
		return new JdbcOrderItemRepository(/*dataSource*/);
	}
	
	public static OrderRepository createOrderRepository(DataSource dataSource,
			OrderItemRepository orderItemRepo) {
		return new JdbcOrderRepository(/*dataSource, */orderItemRepo);
	}
	
	public static ProducerRepository createProducerRepository(DataSource dataSource) {
		return new JdbcProducerRepository(/*dataSource*/);
	}
	
	public static ProductRepository createProductRepository(DataSource dataSource) {
		return new JdbcProductRepository(/*dataSource*/);
	}
	
	private RepositoryFactory() {}
}
