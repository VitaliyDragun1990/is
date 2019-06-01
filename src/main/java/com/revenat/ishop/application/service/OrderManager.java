package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.domain.entity.Order;

/**
 * Contains operation for managing {@link Order} objects in the application
 * 
 * @author Vitaly Dragun
 *
 */
public interface OrderManager {

	long placeOrder(ClientSession clientSession);

	OrderDTO getById(long id, ClientSession clientSession);

	List<OrderDTO> findByClient(ClientSession clientSession, int page, int limit);

	int coundByClient(ClientSession clientSession);

}