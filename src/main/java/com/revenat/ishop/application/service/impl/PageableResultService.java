package com.revenat.ishop.application.service.impl;

import com.revenat.ishop.infrastructure.util.Checks;

abstract class PageableResultService {
	private static final String INVALID_PAGE_NUMBER_MSG_CODE = "message.error.invalidPageNumber";
	private static final String INVALID_LIMIT_VALUE_MSG_CODE = "message.error.invalidLimitValue";

	protected void validateParams(int page, int limit) {
		Checks.checkParam(page >= 1, "page number can not be less that 1: %d", INVALID_PAGE_NUMBER_MSG_CODE, page);
		Checks.checkParam(limit >= 1, "limit can not be less that 1: %d", INVALID_LIMIT_VALUE_MSG_CODE, limit);
	}
}
