package com.revenat.ishop.application.service;

public interface FeedbackService {

	void sendNewOrderNotification(String email, long orderId);
}
