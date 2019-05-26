package com.revenat.ishop.infrastructure.repository.jdbc.transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.handler.DefaultListResultSetHandler;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils.ResultSetHandler;
import com.revenat.ishop.infrastructure.repository.CategoryRepository;

/**
 * This is implementation of the {@link CategoryRepository} that is responsible
 * for performing CRUD on {@link Category} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcCategoryRepository extends AbstractJdbcRepository implements CategoryRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcCategoryRepository.class);
	
	private static final String GET_ALL_CATEGORIES = "SELECT * FROM category ORDER BY name";
	private static final String GET_CATEGORIES_BY_CRITERIA_TEMPLATE = "SELECT c.id, c.name, c.url,"
			+ " CAST(count(prod.id) AS INTEGER) AS product_count "
			+ "FROM category AS c LEFT OUTER JOIN "
			+ "(SELECT p.id, p.category_id FROM product AS p INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "%s) "
			+ "AS prod ON c.id = prod.category_id "
			+ "GROUP BY c.id, c.name, c.url "
			+ "ORDER BY c.name";
	private static final ResultSetHandler<List<Category>> CATEGORIES_HANDLER = new DefaultListResultSetHandler<>(Category.class);

	@Override
	public List<Category> getAll() {
		return execute(conn -> FrameworkJDBCUtils.select(conn, GET_ALL_CATEGORIES, CATEGORIES_HANDLER));
	}

	@Override
	public List<Category> getByCriteria(ProductCriteria criteria) {
		return execute(conn -> {
			SqlQuery sqlQuery = buildSqlQuery(criteria, GET_CATEGORIES_BY_CRITERIA_TEMPLATE);
			LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
			return FrameworkJDBCUtils.select(conn, sqlQuery.getQuery(), CATEGORIES_HANDLER, sqlQuery.getParameters());
		});
	}
}
