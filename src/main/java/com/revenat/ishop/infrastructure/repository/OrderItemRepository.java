package com.revenat.ishop.infrastructure.repository;

import java.util.Collection;
import java.util.List;

import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.infrastructure.framework.annotation.di.JDBCRepository;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.CollectionItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Insert;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link OrderItem} entity.
 * 
 * @author Vitaly Dragun
 *
 */
@JDBCRepository
public interface OrderItemRepository {
	public static final String GET_ORDER_ITEMS_BY_ORDER_ID = 
			"SELECT item.id, item.quantity, item.order_id, item.product_id, "
			+ "p.name, p.description, p.image_link, p.price, c.name AS category, pr.name AS producer "
			+ "FROM order_item AS item "
			+ "INNER JOIN product AS p ON p.id = item.product_id "
			+ "INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "WHERE item.order_id = ?";

	/**
	 * Saves specified {@link OrderItem}
	 * 
	 * @param orderItem order item to save
	 * @return saved order item
	 */
	@Insert
	OrderItem save(OrderItem orderItem);

	/**
	 * Saves all provided {@link OrderItem} entities
	 * 
	 * @param orderItems order items to save
	 */
	@Insert(batchInsert=true)
	void saveAll(Collection<OrderItem> orderItems);

	/**
	 * Returns all {@link OrderItem} entities which belong to order with specified
	 * {@code orderId}
	 * 
	 * @param orderId id of the order to search order items for
	 */
	@Select(GET_ORDER_ITEMS_BY_ORDER_ID)
	@CollectionItem(OrderItem.class)
	List<OrderItem> findByOrderId(long orderId);
}
