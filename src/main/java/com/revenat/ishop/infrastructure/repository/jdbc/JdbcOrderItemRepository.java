package com.revenat.ishop.infrastructure.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.repository.OrderItemRepository;
import com.revenat.ishop.infrastructure.util.JDBCUtils;
import com.revenat.ishop.infrastructure.util.JDBCUtils.ResultSetHandler;

/**
 * This is implementation of the {@link OrderItemRepository} responsible
 * for performing CRUD on {@link OrderItem} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcOrderItemRepository extends AbstractJdbcRepository implements OrderItemRepository {
	private static final ResultSetHandler<Long> GENERATED_ID_HANDLER =
			ResultSetHandlerFactory.GENERATED_LONG_ID_RESULT_SET_HANDLER;
	private static final ResultSetHandler<List<OrderItem>> ORDER_ITEMS_HANDLER =
			ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.ORDER_ITEM_RESULT_SET_HANDLER);

	public JdbcOrderItemRepository(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void save(OrderItem orderItem) {
		Long id = executeUpdate(conn -> JDBCUtils.insert(conn, SqlQueries.INSERT_ORDER_ITEM, GENERATED_ID_HANDLER,
				orderItem.getOrderId(), orderItem.getProduct().getId(), orderItem.getQuantity()));
		orderItem.setId(id);
	}

	@Override
	public void saveAll(Iterable<OrderItem> orderItems) {
		List<Object[]> parametersList = generateParametersList(orderItems);
		executeUpdate(conn -> JDBCUtils.insertBatch(conn, SqlQueries.INSERT_ORDER_ITEM, parametersList));
	}

	private List<Object[]> generateParametersList(Iterable<OrderItem> orderItems) {
		List<Object[]> params = new ArrayList<>();
		for (OrderItem item : orderItems) {
			params.add(new Object[] {item.getOrderId(), item.getProduct().getId(), item.getQuantity()});
		}
		return params;
	}

	@Override
	public List<OrderItem> getByOrderId(long orderId) {
		return executeSelect(conn -> JDBCUtils.select(conn, SqlQueries.GET_ORDER_ITEMS_BY_ORDER_ID, ORDER_ITEMS_HANDLER, orderId));
	}
}
