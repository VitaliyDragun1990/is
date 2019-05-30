package com.revenat.ishop.application.service.impl;

import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;
import com.revenat.ishop.infrastructure.service.NotificationService;

@Component
public class FeedbackServiceImpl implements FeedbackService {
	private static final String TITLE = "New order has been created";
	
	@Autowired
	private NotificationService notificationService;
	@Value("app.host")
	private String applicationHost;
	
	public FeedbackServiceImpl() {
	}
	
	public FeedbackServiceImpl(NotificationService notificationService, String appHost) {
		this.notificationService = notificationService;
		this.applicationHost = appHost;
	}

	@Override
	public void sendNewOrderNotification(String email, long orderId) {
		notificationService.sendNotification(email, TITLE, applicationHost + "/order?id=" + orderId);
	}
}
