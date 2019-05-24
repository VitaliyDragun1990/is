package com.revenat.ishop.infrastructure.repository.jdbc;

import static com.revenat.ishop.infrastructure.repository.jdbc.ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.repository.ProductRepository;
import com.revenat.ishop.infrastructure.util.JDBCUtils;
import com.revenat.ishop.infrastructure.util.JDBCUtils.ResultSetHandler;

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
	private static final ResultSetHandler<Product> PRODUCT_HANDLER =
			ResultSetHandlerFactory.getSingleResultSetHandler(PRODUCT_RESULT_SET_HANDLER);
	private static final ResultSetHandler<Integer> COUNT_HANDLER =
			ResultSetHandlerFactory.COUNT_RESULT_SET_HANDLER;
	private static final ResultSetHandler<List<Product>> PRODUCTS_HANDLER =
			ResultSetHandlerFactory.getListResultSetHandler(PRODUCT_RESULT_SET_HANDLER);

	public JdbcProductRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public int countAll() {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.COUNT_ALL_PRODUCTS, COUNT_HANDLER));
	}
	
	@Override
	public int countByCategory(String categoryUrl) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.COUNT_PRODUCTS_BY_CATEGORY, COUNT_HANDLER, categoryUrl));
	}
	
	@Override
	public int countByCriteria(ProductCriteria criteria) {
		SqlQuery sqlQuery = buildSqlQuery(criteria, SqlQueries.COUNT_PRODUCTS_BY_CRITERIA_TEMPLATE);
		LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
		return executeSelect(conn -> JDBCUtils.select(conn, sqlQuery.getQuery(), COUNT_HANDLER, sqlQuery.getParameters()));
	}
	
	@Override
	public Product getById(Integer id) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_PRODUCT_BY_ID, PRODUCT_HANDLER, id));
	}

	@Override
	public List<Product> getByCategory(String categoryUrl, int offset, int limit) {
		return executeSelect(
				conn -> JDBCUtils.select(conn, SqlQueries.GET_PRODUCTS_BY_CATEGORY, PRODUCTS_HANDLER, categoryUrl, limit, offset)
				);
	}
	
	@Override
	public List<Product> getByCriteria(ProductCriteria criteria, int offset, int limit) {
		SqlQuery sqlQuery = buildSqlQuery(criteria, SqlQueries.GET_PRODUCTS_BY_CRITERIA_TEMPLATE, limit, offset);
		LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
		return executeSelect(
				conn -> JDBCUtils.select(conn, sqlQuery.getQuery(), PRODUCTS_HANDLER,
						sqlQuery.getParameters())
				);
	}
	
	@Override
	public List<Product> getAll(int offset, int limit) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ALL_PRODUCTS, PRODUCTS_HANDLER, limit, offset));
	}
}
