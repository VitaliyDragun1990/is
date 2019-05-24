package com.revenat.ishop.infrastructure.repository.jdbc;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.util.JDBCUtils.ResultSetHandler;

/**
 * This factory class responsible for creating different
 * {@link ResultSetHandler} implementations.
 * 
 * @author Vitaly Dragun
 *
 */
final class ResultSetHandlerFactory {

	/**
	 * This implementation of the {@link ResultSetHandler} interface responsible for
	 * transforming data which {@link ResultSet} instance holds into single instance
	 * of the {@link Product} entity. {@link ResultSet} instance aimed to handle by
	 * this result set handler should have cursor initially positioned at existing
	 * row (prior call to {@link ResultSet#next()} with {@code true} return value is
	 * a necessary prerequisit before passing result set instance to handle by this
	 * handler implementation). In general this implementation should be used either
	 * with {@link #getSingleResultSetHandler(ResultSetHandler)} or with
	 * {@link #getListResultSetHandler(ResultSetHandler)} methods
	 */
	public static final ResultSetHandler<Product> PRODUCT_RESULT_SET_HANDLER = rs -> {
		Product p = new Product();
		p.setId(rs.getInt("id"));
		p.setName(rs.getString("name"));
		p.setDescription(rs.getString("description"));
		p.setImageLink(rs.getString("image_link"));
		p.setPrice(rs.getBigDecimal("price"));
		p.setCategory(rs.getString("category"));
		p.setProducer(rs.getString("producer"));
		return p;
	};
	
	/**
	 * This implementation of the {@link ResultSetHandler} interface responsible for
	 * transforming data which {@link ResultSet} instance holds into single instance
	 * of the {@link Category} entity. {@link ResultSet} instance aimed to handle by
	 * this result set handler should have cursor initially positioned at existing
	 * row (prior call to {@link ResultSet#next()} with {@code true} return value is
	 * a necessary prerequisit before passing result set instance to handle by this
	 * handler implementation). In general this implementation should be used either
	 * with {@link #getSingleResultSetHandler(ResultSetHandler)} or with
	 * {@link #getListResultSetHandler(ResultSetHandler)} methods
	 */
	public static final ResultSetHandler<Category> CATEGORY_RESULT_SET_HANDLER = rs -> {
		Category c = new Category();
		c.setId(rs.getInt("id"));
		c.setName(rs.getString("name"));
		c.setUrl(rs.getString("url"));
		c.setProductCount(rs.getInt("product_count"));
		
		return c;
	};
	
	/**
	 * This implementation of the {@link ResultSetHandler} interface responsible for
	 * transforming data which {@link ResultSet} instance holds into single instance
	 * of the {@link Account} entity. {@link ResultSet} instance aimed to handle by
	 * this result set handler should have cursor initially positioned at existing
	 * row (prior call to {@link ResultSet#next()} with {@code true} return value is
	 * a necessary prerequisit before passing result set instance to handle by this
	 * handler implementation). In general this implementation should be used either
	 * with {@link #getSingleResultSetHandler(ResultSetHandler)} or with
	 * {@link #getListResultSetHandler(ResultSetHandler)} methods
	 */
	public static final ResultSetHandler<Account> ACCOUNT_RESULT_SET_HANDLER = rs -> {
		Account a = new Account();
		a.setId(rs.getInt("id"));
		a.setName(rs.getString("name"));
		a.setEmail(rs.getString("email"));
		a.setAvatarUrl(rs.getString("avatar_url"));
		
		return a;
	};
	
	/**
	 * This implementation of the {@link ResultSetHandlerFactory} responsible for
	 * extracting {@code count} field from {@link ResultSet} row data.
	 */
	public static final ResultSetHandler<Integer> COUNT_RESULT_SET_HANDLER = rs -> {
		if (rs.next()) {
			return rs.getInt("count");
		} else {
			return 0;
		}
	};
	
	/**
	 * This implementation of the {@link ResultSetHandlerFactory} responsible for
	 * extracting {@code count} field from {@link ResultSet} row data.
	 */
	public static final ResultSetHandler<Integer> GENERATED_INT_ID_RESULT_SET_HANDLER = rs -> {
		if (rs.next()) {
			return rs.getInt("id");
		} else {
			throw new PersistenceException("Error while retrieving auto-generated key value: no key has been generated");
		}
	};
	
	/**
	 * This implementation of the {@link ResultSetHandlerFactory} responsible for
	 * extracting {@code count} field from {@link ResultSet} row data.
	 */
	public static final ResultSetHandler<Long> GENERATED_LONG_ID_RESULT_SET_HANDLER = rs -> {
		if (rs.next()) {
			return rs.getLong("id");
		} else {
			throw new PersistenceException("Error while retrieving auto-generated key value: no key has been generated");
		}
	};
	
	/**
	 * This implementation of the {@link ResultSetHandler} interface responsible for
	 * transforming data which {@link ResultSet} instance holds into single instance
	 * of the {@link Producer} entity. {@link ResultSet} instance aimed to handle by
	 * this result set handler should have cursor initially positioned at existing
	 * row (prior call to {@link ResultSet#next()} with {@code true} return value is
	 * a necessary prerequisit before passing result set instance to handle by this
	 * handler implementation). In general this implementation should be used either
	 * with {@link #getSingleResultSetHandler(ResultSetHandler)} or with
	 * {@link #getListResultSetHandler(ResultSetHandler)} methods
	 */
	public static final ResultSetHandler<Producer> PRODUCER_RESULT_SET_HANDLER = rs -> {
		Producer p = new Producer();
		p.setId(rs.getInt("id"));
		p.setName(rs.getString("name"));
		p.setProductCount(rs.getInt("product_count"));
		
		return p;
	};
	
	/**
	 * This implementation of the {@link ResultSetHandler} interface responsible for
	 * transforming data that {@link ResultSet} instance holds into single instance
	 * of the {@link Order} entity. {@link ResultSet} instance aimed to handle by
	 * this result set handler should have cursor initially positioned at existing
	 * row (prior call to {@link ResultSet#next()} with {@code true} return value is
	 * a necessary prerequisit before passing result set instance to handle by this
	 * handler implementation). In general this implementation should be used either
	 * with {@link #getSingleResultSetHandler(ResultSetHandler)} or with
	 * {@link #getListResultSetHandler(ResultSetHandler)} methods
	 */
	public static final ResultSetHandler<Order> ORDER_RESULT_SET_HANDLER = rs -> {
		Order order = new Order();
		order.setId(rs.getLong("id"));
		order.setCreated(rs.getObject("created", LocalDateTime.class));
		order.setAccountId(rs.getInt("account_id"));
		
		return order;
	};
	
	/**
	 * This implementation of the {@link ResultSetHandler} interface responsible for
	 * transforming data that {@link ResultSet} instance holds into single instance
	 * of the {@link OrderItem} entity. {@link ResultSet} instance aimed to handle by
	 * this result set handler should have cursor initially positioned at existing
	 * row (prior call to {@link ResultSet#next()} with {@code true} return value is
	 * a necessary prerequisit before passing result set instance to handle by this
	 * handler implementation). In general this implementation should be used either
	 * with {@link #getSingleResultSetHandler(ResultSetHandler)} or with
	 * {@link #getListResultSetHandler(ResultSetHandler)} methods
	 */
	public static final ResultSetHandler<OrderItem> ORDER_ITEM_RESULT_SET_HANDLER =rs -> {
		OrderItem item = new OrderItem();
		item.setId(rs.getLong("item_id"));
		item.setQuantity(rs.getInt("quantity"));
		item.setOrderId(rs.getLong("order_id"));
		Product p = PRODUCT_RESULT_SET_HANDLER.handle(rs);
		item.setProduct(p);
		
		return item;
	};

	/**
	 * Returns {@link ResultSetHandler} implementation that capable of tarnsforming
	 * {@link ResultSet} data into single instance of specified type.
	 * 
	 * @param oneRowResultSetHandler {@link ResultSetHandler} implementation that
	 *                               can transform {@link ResultSet} row data into
	 *                               instance of specified type.
	 */
	public static final <T> ResultSetHandler<T> getSingleResultSetHandler(
			final ResultSetHandler<T> oneRowResultSetHandler) {
		return rs -> {
			if (rs.next()) {
				return oneRowResultSetHandler.handle(rs);
			}
			return null;
		};
	}

	/**
	 * Returns {@link ResultSetHandler} implementation that capable of tarnsforming
	 * {@link ResultSet} data into {@link List} with instances of specified type.
	 * 
	 * @param oneRowResultSetHandler {@link ResultSetHandler} implementation that
	 *                               can transform {@link ResultSet} row data into
	 *                               instance of specified type.
	 */
	public static final <T> ResultSetHandler<List<T>> getListResultSetHandler(
			final ResultSetHandler<T> oneRowResultSetHandler) {
		return rs -> {
			List<T> list = new ArrayList<>();
			while (rs.next()) {
				list.add(oneRowResultSetHandler.handle(rs));
			}
			return list;
		};
	}

	private ResultSetHandlerFactory() {
	}
}
