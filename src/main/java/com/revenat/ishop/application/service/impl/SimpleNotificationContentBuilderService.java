package com.revenat.ishop.application.service.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.application.service.NotificationContentBuilderService;

@Service
public class SimpleNotificationContentBuilderService implements NotificationContentBuilderService {
	private String applicationHost;
	private I18nService i18nService;
	
	@Autowired
	public SimpleNotificationContentBuilderService(
			@Value("${app.host}") String applicationHost,
			I18nService i18nService) {
		this.applicationHost = applicationHost;
		this.i18nService = i18nService;
	}

	@Override
	public String buildNewOrderCreatedNotificationMessage(OrderDTO order, Locale locale) {
		String link = applicationHost + "/order?id=" + order.getId();
		return i18nService.getMessage("notification.newOrder.content", locale, link);
	}

}
