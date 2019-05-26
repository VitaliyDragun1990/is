package com.revenat.ishop.infrastructure.repository.jdbc.transactional;

import java.util.ArrayList;
import java.util.List;

import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.framework.handler.DefaultListResultSetHandler;
import com.revenat.ishop.infrastructure.framework.handler.DefaultUniqueResultSetHandler;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils;
import com.revenat.ishop.infrastructure.framework.util.FrameworkJDBCUtils.ResultSetHandler;
import com.revenat.ishop.infrastructure.repository.OrderItemRepository;

/**
 * This is implementation of the {@link OrderItemRepository} responsible
 * for performing CRUD on {@link OrderItem} entities using underlaying
 * relational database of some sort and a JDBC technology to interract with it.
 * 
 * @author Vitaly Dragun
 *
 */
public class JdbcOrderItemRepository extends AbstractJdbcRepository implements OrderItemRepository {
	private static final String GET_ORDER_ITEMS_BY_ORDER_ID = "SELECT item.id, item.quantity, item.order_id, item.product_id, "
			+ "p.name, p.description, p.image_link, p.price, c.name AS category, pr.name AS producer "
			+ "FROM order_item AS item "
			+ "INNER JOIN product AS p ON p.id = item.product_id "
			+ "INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "WHERE item.order_id = ?";
	private static final String INSERT_ORDER_ITEM = "INSERT INTO order_item (order_id, product_id, quantity) "
			+ "VALUES (?,?,?)";
	private static final ResultSetHandler<OrderItem> ORDER_ITEM_HANDLER = new DefaultUniqueResultSetHandler<>(OrderItem.class);
	private static final ResultSetHandler<List<OrderItem>> ORDER_ITEMS_HANDLER = new DefaultListResultSetHandler<>(OrderItem.class);

	@Override
	public OrderItem save(OrderItem orderItem) {
		OrderItem saved = execute(conn -> FrameworkJDBCUtils.insert(conn, INSERT_ORDER_ITEM, ORDER_ITEM_HANDLER,
				orderItem.getOrderId(), orderItem.getProduct().getId(), orderItem.getQuantity()));
		orderItem.setId(saved.getId());
		return orderItem;
	}

	@Override
	public void saveAll(Iterable<OrderItem> orderItems) {
		List<Object[]> parametersList = generateParametersList(orderItems);
		execute(conn -> FrameworkJDBCUtils.insertBatch(conn, INSERT_ORDER_ITEM, parametersList));
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
		return execute(conn -> FrameworkJDBCUtils.select(conn, GET_ORDER_ITEMS_BY_ORDER_ID, ORDER_ITEMS_HANDLER, orderId));
	}
}
