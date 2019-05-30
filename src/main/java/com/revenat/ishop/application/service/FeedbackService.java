package com.revenat.ishop.application.service;

import com.revenat.ishop.domain.entity.Order;

public interface FeedbackService {

	void sendNewOrderNotification(String email, Order order);
}
