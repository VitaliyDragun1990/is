package com.revenat.ishop.service.domain;

import java.util.List;

import com.revenat.ishop.entity.Order;
import com.revenat.ishop.model.ShoppingCart;

public interface OrderService {
	
	Order createOrder(ShoppingCart cart, int accountId);
	
	Order getById(long id);
	
	List<Order> getByAccountId(int accountId, int page, int limit);
	
	int countByAccountId(int accountId);
}
