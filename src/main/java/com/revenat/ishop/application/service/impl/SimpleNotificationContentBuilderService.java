package com.revenat.ishop.application.service.impl;

import java.util.Locale;

import com.revenat.ishop.application.dto.OrderDTO;
import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.application.service.NotificationContentBuilderService;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;

@Component
public class SimpleNotificationContentBuilderService implements NotificationContentBuilderService {
	
	@Value("app.host")
	private String applicationHost;
	@Autowired
	private I18nService i18nService;
	
	public SimpleNotificationContentBuilderService() {
	}
	
	public SimpleNotificationContentBuilderService(String applicationHost, I18nService i18nService) {
		this.applicationHost = applicationHost;
		this.i18nService = i18nService;
	}

	@Override
	public String buildNewOrderCreatedNotificationMessage(OrderDTO order, Locale locale) {
		String link = applicationHost + "/order?id=" + order.getId();
		return i18nService.getMessage("notification.newOrder.content", locale, link);
	}

}
