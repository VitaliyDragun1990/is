package com.revenat.ishop.application.service;

import java.util.Locale;

import com.revenat.ishop.application.dto.OrderDTO;

public interface NotificationContentBuilderService {

	String buildNewOrderCreatedNotificationMessage(OrderDTO order, Locale locale);
}
