package com.revenat.ishop.application.service.impl;

import com.revenat.ishop.application.service.NotificationContentBuilderService;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;

@Component
public class SimpleNotificationContentBuilderService implements NotificationContentBuilderService {
	
	@Value("app.host")
	private String applicationHost;
	
	public SimpleNotificationContentBuilderService() {
	}
	
	public SimpleNotificationContentBuilderService(String applicationHost) {
		this.applicationHost = applicationHost;
	}

	@Override
	public String buildNewOrderCreatedNotificationMessage(Order order) {
		return applicationHost + "/order?id=" + order.getId();
	}

}