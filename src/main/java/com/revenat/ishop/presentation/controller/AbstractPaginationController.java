package com.revenat.ishop.presentation.controller;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractPaginationController extends AbstractController {
	private static final long serialVersionUID = 3886146353255295460L;
	
	protected final int getPage(HttpServletRequest req) {
		try {
			return Integer.parseInt(req.getParameter("page"));
		} catch (NumberFormatException e) {
			// If page format not an integer, return 1 page
			return 1;
		}
	}
	
	protected final int getTotalPageCount(int totalItemsCount, int itemsPerPage) {
		int totalPageCount = totalItemsCount / itemsPerPage;
		if (totalPageCount * itemsPerPage != totalItemsCount) {
			totalPageCount++;
		}
		
		return totalPageCount;
	}
}
