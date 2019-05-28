package com.revenat.ishop.infrastructure.repository;

import com.revenat.ishop.infrastructure.framework.factory.repository.JDBCRepositoryFactory;

public final class RepositoryFactory {

	public static AccountRepository createAccountRepository(/*DataSource dataSource*/) {
//		return new JdbcAccountRepository(/*dataSource*/);
		return JDBCRepositoryFactory.createRepository(AccountRepository.class);
	}
	
	public static CategoryRepository createCategoryRepository(/*DataSource dataSource*/) {
//		return new JdbcCategoryRepository(/*dataSource*/);
/*		return new XMLCategoryRepository(
				createProductRepository(),
				createProducerRepository(),
				"E:/java/eclipse_workspace_02/ishop/external/categories.xml");*/
		return JDBCRepositoryFactory.createRepository(CategoryRepository.class);
	}
	
	public static OrderItemRepository createOrderItemRepository(/*DataSource dataSource*/) {
//		return new JdbcOrderItemRepository(/*dataSource*/);
		return JDBCRepositoryFactory.createRepository(OrderItemRepository.class);
	}
	
	public static OrderRepository createOrderRepository(/*DataSource dataSource,*/) {
//		return new JdbcOrderRepository(/*dataSource, */);
		return JDBCRepositoryFactory.createRepository(OrderRepository.class);
	}
	
	public static ProducerRepository createProducerRepository(/*DataSource dataSource*/) {
//		return new JdbcProducerRepository(/*dataSource*/);
		return JDBCRepositoryFactory.createRepository(ProducerRepository.class);
	}
	
	public static ProductRepository createProductRepository(/*DataSource dataSource*/) {
//		return new JdbcProductRepository(/*dataSource*/);
		return JDBCRepositoryFactory.createRepository(ProductRepository.class);
	}
	
	private RepositoryFactory() {}
}
