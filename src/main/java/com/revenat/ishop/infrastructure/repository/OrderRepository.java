package com.revenat.ishop.infrastructure.repository;

import java.util.List;

import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.infrastructure.framework.annotation.di.JDBCRepository;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.CollectionItem;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Insert;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;

/**
 * This interface represents repository responsible for performing CRUD
 * operations on {@link Order} entity.
 * 
 * @author Vitaly Dragun
 *
 */
@JDBCRepository
public interface OrderRepository {

	@Insert
	Order save(Order order);

	@Select("SELECT * FROM \"order\" WHERE id = ?")
	Order findById(long id);

	@Select("SELECT * FROM \"order\" WHERE account_id = ? ORDER BY created DESC LIMIT ? OFFSET ?")
	@CollectionItem(Order.class)
	List<Order> findByAccountId(int accountId, int limit, int offset);

	@Select("SELECT count(*) AS count FROM \"order\" WHERE account_id = ?")
	int countByAccountId(int accountId);
}
