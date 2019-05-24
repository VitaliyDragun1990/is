package com.revenat.ishop.application.service.impl;

import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.infrastructure.service.NotificationService;

class FeedbackServiceImpl implements FeedbackService {
	private static final String TITLE = "New order has been created";
	
	private final NotificationService notificationService;
	private final String applicationHost;
	
	public FeedbackServiceImpl(NotificationService notificationService, String appHost) {
		this.notificationService = notificationService;
		this.applicationHost = appHost;
	}

	@Override
	public void sendNewOrderNotification(String email, long orderId) {
		notificationService.sendNotification(email, TITLE, applicationHost + "/order?id=" + orderId);

	}
}
