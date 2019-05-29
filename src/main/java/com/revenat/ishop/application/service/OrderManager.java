package com.revenat.ishop.application.service;

import java.util.List;

import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.domain.entity.Order;

public interface OrderManager {

	long placeOrder(ClientSession clientSession);

	Order getById(long id, ClientSession clientSession);

	List<Order> findByClient(ClientSession clientSession, int page, int limit);

	int coundByClient(ClientSession clientSession);

}