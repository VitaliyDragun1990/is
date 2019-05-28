package com.revenat.ishop.infrastructure.repository.jdbc.transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.framework.handler.DefaultListResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.ResultSetHandler;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;

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
	
	private static final String GET_ALL_PRODUCERS = "SELECT * FROM producer ORDER BY name";
	private static final String GET_PRODUCERS_BY_CRITERIA_TEMPLATE = "SELECT pr.id, pr.name, "
			+ "CAST(count(prod.id) AS INTEGER) AS product_count "
			+ "FROM producer AS pr LEFT OUTER JOIN "
			+ "(SELECT p.id, p.producer_id FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "%s) "
			+ "AS prod ON pr.id = prod.producer_id "
			+ "GROUP BY pr.id, pr.name "
			+ "ORDER BY pr.name";
	private static final ResultSetHandler<List<Producer>> PRODUCERS_HANDLER = new DefaultListResultSetHandler<>(Producer.class);

	@Override
	public List<Producer> findAll() {
		return execute(conn -> FrameworkJDBCUtils.select(conn, GET_ALL_PRODUCERS, PRODUCERS_HANDLER));
	}
	
	@Override
	public List<Producer> findByCriteria(ProductCriteria criteria) {
		return execute(conn -> {
			SqlQuery sqlQuery = buildSqlQuery(criteria, GET_PRODUCERS_BY_CRITERIA_TEMPLATE);
			LOGGER.debug("search query={} with params={}", sqlQuery.getQuery(), sqlQuery.getParameters());
			return FrameworkJDBCUtils.select(conn, sqlQuery.getQuery(), PRODUCERS_HANDLER, sqlQuery.getParameters());
		});
	}
}
