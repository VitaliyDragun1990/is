package com.revenat.ishop.application.service;

import com.revenat.ishop.application.dto.OrderDTO;

public interface NotificationContentBuilderService {

	String buildNewOrderCreatedNotificationMessage(OrderDTO order);
}
