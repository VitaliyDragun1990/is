package com.revenat.ishop.application.service.impl;

import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.application.service.NotificationContentBuilderService;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.service.NotificationService;

@Component
public class FeedbackServiceImpl implements FeedbackService {
	private static final String TITLE = "New order has been created";
	
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private NotificationContentBuilderService contentBuilder;
	
	public FeedbackServiceImpl() {
	}
	
	public FeedbackServiceImpl(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@Override
	public void sendNewOrderNotification(String email, Order order) {
		notificationService.sendNotification(email, TITLE, contentBuilder.buildNewOrderCreatedNotificationMessage(order));
	}
}
