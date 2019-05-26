package com.revenat.ishop.infrastructure.repository.jdbc.plain;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.util.JDBCUtils;
import com.revenat.ishop.infrastructure.util.JDBCUtils.ResultSetHandler;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.base.AbstractJdbcRepository;

/**
 * This is implementation of the {@link ProducerRepository} responsible
 * for performing CRUD operations on {@link Producer} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcProducerRepository extends AbstractJdbcRepository implements ProducerRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcProducerRepository.class);
	private static final ResultSetHandler<List<Producer>> PRODUCERS_HANDLER =
			ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCER_RESULT_SET_HANDLER);
	
	public JdbcProducerRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Producer> getAll() {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ALL_PRODUCERS, PRODUCERS_HANDLER));
	}
	
	@Override
	public List<Producer> getByCriteria(ProductCriteria criteria) {
		return executeSelect(conn -> {
			SqlQuery sqlQuery = buildSqlQuery(criteria, SqlQueries.GET_PRODUCERS_BY_CRITERIA_TEMPLATE);
			LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
			return JDBCUtils.select(conn, sqlQuery.getQuery(), PRODUCERS_HANDLER, sqlQuery.getParameters());
		});
	}
}
