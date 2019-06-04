package com.revenat.ishop.application.service.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.application.service.NotificationContentBuilderService;
import com.revenat.ishop.infrastructure.service.NotificationService;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	
	private NotificationService notificationService;
	private NotificationContentBuilderService contentBuilder;
	private I18nService i18nService;
	
	@Autowired
	public FeedbackServiceImpl(
			NotificationService notificationService,
			NotificationContentBuilderService contentBuilder,
			I18nService i18nService) {
		this.notificationService = notificationService;
		this.contentBuilder = contentBuilder;
		this.i18nService = i18nService;
	}

	@Override
	public void sendNewOrderNotification(String clientEmail, Locale clientLocale, OrderDTO order) {
		String title = i18nService.getMessage("notification.newOrder.title", clientLocale);
		notificationService.sendNotification(
				clientEmail,
				title,
				contentBuilder.buildNewOrderCreatedNotificationMessage(order, clientLocale));
	}
}
