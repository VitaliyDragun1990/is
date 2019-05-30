package com.revenat.ishop.application.service;

import com.revenat.ishop.domain.entity.Order;

public interface NotificationContentBuilderService {

	String buildNewOrderCreatedNotificationMessage(Order order);
}
