package com.revenat.ishop.persistence.repository.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.application.domain.entity.Category;
import com.revenat.ishop.application.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.persistence.infra.util.JDBCUtils;
import com.revenat.ishop.persistence.infra.util.JDBCUtils.ResultSetHandler;
import com.revenat.ishop.persistence.repository.CategoryRepository;

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
	private static final ResultSetHandler<List<Category>> CATEGORIES_HANDLER =
			ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.CATEGORY_RESULT_SET_HANDLER);
	
	public JdbcCategoryRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Category> getAll() {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ALL_CATEGORIES, CATEGORIES_HANDLER));
	}

	@Override
	public List<Category> getByCriteria(ProductCriteria criteria) {
		return executeSelect(conn -> {
			SqlQuery sqlQuery = buildSqlQuery(criteria, SqlQueries.GET_CATEGORIES_BY_CRITERIA_TEMPLATE);
			LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
			return JDBCUtils.select(conn, sqlQuery.getQuery(), CATEGORIES_HANDLER, sqlQuery.getParameters());
		});
	}
}
