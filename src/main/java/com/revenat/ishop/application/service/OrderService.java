package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.domain.entity.OrderItem;

public interface OrderService {
	
	OrderDTO createOrder(List<OrderItem> orderItems, int accountId);
	
	OrderDTO findById(long id);
	
	List<OrderDTO> findByAccountId(int accountId, int page, int limit);
	
	int countByAccountId(int accountId);
}
