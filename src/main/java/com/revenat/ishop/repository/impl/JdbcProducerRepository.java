package com.revenat.ishop.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.repository.ProducerRepository;
import com.revenat.ishop.search.criteria.ProductCriteria;
import com.revenat.ishop.util.jdbc.JDBCUtils;
import com.revenat.ishop.util.jdbc.JDBCUtils.ResultSetHandler;

/**
 * This is implementation of the {@link ProducerRepository} that is responsible
 * for CRUD management of the {@link Producer} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcProducerRepository extends JdbcRepository implements ProducerRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcProducerRepository.class);
	private static final ResultSetHandler<List<Producer>> PRODUCERS_HANDLER =
			ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCER_RESULT_SET_HANDLER);
	
	public JdbcProducerRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public List<Producer> getAll() {
		return execute(conn -> JDBCUtils.select(conn, SqlQueries.GET_ALL_PRODUCERS, PRODUCERS_HANDLER));
	}
	
	@Override
	public List<Producer> getByCriteria(ProductCriteria criteria) {
		return execute(conn -> {
			SqlQuery sqlQuery = buildSqlQuery(criteria, SqlQueries.GET_PRODUCERS_BY_CRITERIA_TEMPLATE);
			LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
			return JDBCUtils.select(conn, sqlQuery.getQuery(), PRODUCERS_HANDLER, sqlQuery.getParameters());
		});
	}
}
