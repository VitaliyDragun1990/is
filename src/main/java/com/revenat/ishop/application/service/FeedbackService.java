package com.revenat.ishop.application.service;

import java.util.Locale;

import com.revenat.ishop.application.dto.OrderDTO;

public interface FeedbackService {

	void sendNewOrderNotification(String clientEmail, Locale clientLocale, OrderDTO order);
}
