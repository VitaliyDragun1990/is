package com.revenat.ishop.infrastructure.repository.jdbc.plain;

import java.util.List;

import javax.sql.DataSource;

import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.util.JDBCUtils;
import com.revenat.ishop.infrastructure.util.JDBCUtils.ResultSetHandler;
import com.revenat.ishop.infrastructure.repository.OrderItemRepository;
import com.revenat.ishop.infrastructure.repository.OrderRepository;
import com.revenat.ishop.infrastructure.repository.jdbc.base.AbstractJdbcRepository;

/**
 * This is implementation of the {@link OrderRepository} responsible
 * for performing CRUD on {@link OrderItem} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcOrderRepository extends AbstractJdbcRepository implements OrderRepository {
	private static final ResultSetHandler<Long> GENERATED_ID_HANDLER =
			ResultSetHandlerFactory.getIdResultSetHandler("id");
	private static final ResultSetHandler<Order> ORDER_HANDLER =
			ResultSetHandlerFactory.getSingleResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
	private static final ResultSetHandler<List<Order>> ORDERS_NANDLER =
			ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_RESULT_SET_HANDLER);
	private static final ResultSetHandler<Integer> COUNT_HANDLER =
			ResultSetHandlerFactory.COUNT_RESULT_SET_HANDLER;
	
	private final OrderItemRepository orderItemRepo;

	public JdbcOrderRepository(DataSource dataSource, OrderItemRepository orderItemRepository) {
		super(dataSource);
		this.orderItemRepo = orderItemRepository;
	}

	@Override
	public Order save(Order order) {
		// save order and get its generated it
		Long orderId = executeUpdate(conn ->
			JDBCUtils.insert(conn, SqlQueries.INSERT_ORDER, GENERATED_ID_HANDLER, order.getAccountId(), order.getCreated())
		);
		// set newly generated id
		order.setId(orderId);
		// save order's order-items if any
		saveOrderItemsIfAny(order.getItems(), orderId);
		return order;
	}
	
	private void saveOrderItemsIfAny(List<OrderItem> orderItems, Long orderId) {
		if (!orderItems.isEmpty()) {
			// set this id for every orderItem inside order
			orderItems.forEach(item -> item.setOrderId(orderId));
			// save all orderItems via orderItemRepository
			orderItemRepo.saveAll(orderItems);
		}
	}

	@Override
	public Order getById(long id) {
		// Get order by id
		Order order = executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ORDER_BY_ID, ORDER_HANDLER, id));
		if (order != null) {
			loadOrderItemsForOrder(order);
		}
		// Return such order
		return order;
	}

	@Override
	public List<Order> getByAccountId(int accountId, int offset, int limit) {
		// Get order by accountId
		List<Order> orders = executeSelect(conn ->
			JDBCUtils.select(conn, SqlQueries.GET_ORDERS_BY_ACCOUNT_ID, ORDERS_NANDLER, accountId, limit, offset)
		);
		for (Order order : orders) {
			loadOrderItemsForOrder(order);
		}
		// Return such orders
		return orders;
	}
	
	@Override
	public int countByAccountId(int accountId) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.COUNT_ORDERS_BY_ACCOUNT_ID, COUNT_HANDLER, accountId));
	}
	
	private void loadOrderItemsForOrder(Order order) {
		// Get all orderItems for such order
		List<OrderItem> orderItems = orderItemRepo.getByOrderId(order.getId());
		// Add these orderItems to order
		order.setItems(orderItems);
	}
}
