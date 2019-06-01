package com.revenat.ishop.application.service;

import com.revenat.ishop.application.dto.OrderDTO;

public interface FeedbackService {

	void sendNewOrderNotification(String email, OrderDTO order);
}
