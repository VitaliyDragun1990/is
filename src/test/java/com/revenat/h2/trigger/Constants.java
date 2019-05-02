package com.revenat.h2.trigger;

class Constants {
	public static final String PRODUCER_ID = "producer_id";
	public static final String CATEGORY_ID = "category_id";
	public static final String UPDATE_PRODUCER_INCREMENT_PRODUCT_COUNT =
			"UPDATE producer SET product_count = product_count + 1 WHERE id = ?";
	public static final String UPDATE_PRODUCER_DECREMENT_PRODUCT_COUNT =
			"UPDATE producer SET product_count = product_count - 1 WHERE id = ?";
	public static final String UPDATE_CATEGORY_INCREMENT_PRODUCT_COUNT =
			"UPDATE category SET product_count = product_count + 1 WHERE id = ?";
	public static final String UPDATE_CATEGORY_DECREMENT_PRODUCT_COUNT =
			"UPDATE category SET product_count = product_count - 1 WHERE id = ?";
	
	private Constants() {}
}
