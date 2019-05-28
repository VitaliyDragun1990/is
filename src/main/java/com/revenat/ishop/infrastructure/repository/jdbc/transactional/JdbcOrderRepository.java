package com.revenat.ishop.infrastructure.repository.jdbc.transactional;

import java.util.List;

import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.framework.handler.DefaultListResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.DefaultUniqueResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.IntResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.ResultSetHandler;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils;
import com.revenat.ishop.infrastructure.repository.OrderRepository;

/**
 * This is implementation of the {@link OrderRepository} responsible
 * for performing CRUD on {@link OrderItem} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcOrderRepository extends AbstractJdbcRepository implements OrderRepository {
	private static final String GET_ORDER_BY_ID = "SELECT * FROM \"order\" WHERE id = ?";
	private static final String GET_ORDERS_BY_ACCOUNT_ID = "SELECT * FROM \"order\" WHERE account_id = ? "
			+ "ORDER BY created DESC LIMIT ? OFFSET ?";
	private static final String COUNT_ORDERS_BY_ACCOUNT_ID = "SELECT count(*) AS count "
			+ "FROM \"order\" WHERE account_id = ?";
	private static final String INSERT_ORDER = "INSERT INTO \"order\" (account_id, created) "
			+ "VALUES (?,?)";
	private static final ResultSetHandler<Order> ORDER_HANDLER = new DefaultUniqueResultSetHandler<>(Order.class);
	private static final ResultSetHandler<List<Order>> ORDERS_HANDLER = new DefaultListResultSetHandler<>(Order.class);
	private static final ResultSetHandler<Integer> COUNT_HANDLER = new IntResultSetHandler();
	

	@Override
	public Order save(Order order) {
		// save order and get its generated it
		Order saved = execute(conn ->
			FrameworkJDBCUtils.insert(conn, INSERT_ORDER, ORDER_HANDLER, order.getAccountId(), order.getCreated())
		);
		// set newly generated id
		order.setId(saved.getId());
		return order;
	}

	@Override
	public Order findById(long id) {
		return execute(conn -> FrameworkJDBCUtils.select(conn, GET_ORDER_BY_ID, ORDER_HANDLER, id));
	}

	@Override
	public List<Order> findByAccountId(int accountId, int offset, int limit) {
		return execute(conn ->
			FrameworkJDBCUtils.select(conn, GET_ORDERS_BY_ACCOUNT_ID, ORDERS_HANDLER, accountId, limit, offset)
		);
	}
	
	@Override
	public int countByAccountId(int accountId) {
		return execute(conn -> FrameworkJDBCUtils.select(conn, COUNT_ORDERS_BY_ACCOUNT_ID, COUNT_HANDLER, accountId));
	}
}
