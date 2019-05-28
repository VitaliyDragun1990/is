package com.revenat.ishop.infrastructure.repository.jdbc.framework;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.handler.DefaultListResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.DefaultUniqueResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.IntResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.ResultSetHandler;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils;
import com.revenat.ishop.infrastructure.repository.ProductRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.base.AbstractJdbcRepository;

/**
 * This is implementation of the {@link ProductRepository} responsible
 * for performing CRUD operations on {@link Product} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcProductRepository extends AbstractJdbcRepository implements ProductRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcProductRepository.class);
	
	private static final String GET_PRODUCT_BY_ID = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "WHERE p.id = ? ORDER BY p.id";
	private static final String GET_ALL_PRODUCTS = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	private static final String GET_PRODUCTS_BY_CATEGORY = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id " + "WHERE c.url = ? "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	private static final String GET_PRODUCTS_BY_CRITERIA_TEMPLATE = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ " %s "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	private static final String COUNT_ALL_PRODUCTS = "SELECT count(*) as count FROM product";
	private static final String COUNT_PRODUCTS_BY_CATEGORY = "SELECT product_count AS count FROM category AS c "
			+ "WHERE c.url = ?";
	private static final String COUNT_PRODUCTS_BY_CRITERIA_TEMPLATE = "SELECT count(*) AS count "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "%s";
	private static final ResultSetHandler<Product> PRODUCT_HANDLER = new DefaultUniqueResultSetHandler<>(Product.class);
	private static final ResultSetHandler<List<Product>> PRODUCTS_HANDLER = new DefaultListResultSetHandler<>(Product.class);;
	private static final ResultSetHandler<Integer> COUNT_HANDLER = new IntResultSetHandler();

	public JdbcProductRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public int countAll() {
		return executeSelect(conn -> FrameworkJDBCUtils.select(conn, COUNT_ALL_PRODUCTS, COUNT_HANDLER));
	}
	
	@Override
	public int countByCategory(String categoryUrl) {
		return executeSelect(conn -> FrameworkJDBCUtils.select(conn, COUNT_PRODUCTS_BY_CATEGORY, COUNT_HANDLER, categoryUrl));
	}
	
	@Override
	public int countByCriteria(ProductCriteria criteria) {
		SqlQuery sqlQuery = buildSqlQuery(criteria, COUNT_PRODUCTS_BY_CRITERIA_TEMPLATE);
		LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
		return executeSelect(conn -> FrameworkJDBCUtils.select(conn, sqlQuery.getQuery(), COUNT_HANDLER, sqlQuery.getParameters()));
	}
	
	@Override
	public Product findById(Integer id) {
		return executeSelect(conn -> FrameworkJDBCUtils.select(conn, GET_PRODUCT_BY_ID, PRODUCT_HANDLER, id));
	}

	@Override
	public List<Product> findByCategory(String categoryUrl, int limit, int offset) {
		return executeSelect(
				conn -> FrameworkJDBCUtils.select(conn, GET_PRODUCTS_BY_CATEGORY, PRODUCTS_HANDLER, categoryUrl, limit, offset)
				);
	}
	
	@Override
	public List<Product> findByCriteria(ProductCriteria criteria, int limit, int offset) {
		SqlQuery sqlQuery = buildSqlQuery(criteria, GET_PRODUCTS_BY_CRITERIA_TEMPLATE, limit, offset);
		LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
		return executeSelect(
				conn -> FrameworkJDBCUtils.select(conn, sqlQuery.getQuery(), PRODUCTS_HANDLER,
						sqlQuery.getParameters())
				);
	}
	
	@Override
	public List<Product> findAll(int limit, int offset) {
		return executeSelect(conn -> FrameworkJDBCUtils.select(conn, GET_ALL_PRODUCTS, PRODUCTS_HANDLER, limit, offset));
	}
}
