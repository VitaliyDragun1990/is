package com.revenat.ishop.application.service.impl;

import java.util.Locale;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.FeedbackService;
import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.application.service.NotificationContentBuilderService;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.service.NotificationService;

@Component
public class FeedbackServiceImpl implements FeedbackService {
	
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private NotificationContentBuilderService contentBuilder;
	@Autowired
	private I18nService i18nService;
	
	public FeedbackServiceImpl() {
	}
	
	public FeedbackServiceImpl(NotificationService notificationService,
			NotificationContentBuilderService contentBuilder, I18nService i18nService) {
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
