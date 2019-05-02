package com.revenat.ishop.config;

public final class Constants {
	public static final int MAX_PRODUCT_COUNT_PER_SHOPPING_CART = 10;
	public static final int MAX_PRODUCTS_PER_SHOPPING_CART = 20;
	public static final int MAX_PRODUCTS_PER_HTML_PAGE = 12;
	public static final int SESSION_MAX_INACTIVE_INTERVAL = 30 * 60;
	public static final String APPLICATION_PROPERTIES = "application.properties";

	
	public static class Attribute {
		public static final String ACCOUNT_REQUEST_STATISTICS = "ACCOUNT_REQUEST_STATISTICS";
		public static final String SERVICE_MANAGER = "SERVICE_MAMANGER";
		public static final String CURRENT_SHOPPING_CART = "CURRENT_SHOPPING_CART";
		public static final String ALL_CATEGORIES = "allCategories";
		public static final String ALL_PRODUCERS = "allProducers";
		public static final String FILTER_CATEGORIES = "filterCategories";
		public static final String FILTER_PRODUCERS = "filterProducers";
		public static final String TOTAL_PAGE_COUNT = "totalPageCount";
		
		
		private Attribute() {}
	}
	
	public static class Directory {
		public static final String JSP = "/WEB-INF/jsp/";
		public static final String FRAGMENT = JSP + "fragment/";
		public static final String PAGE = JSP + "page/";
		
		private Directory() {}
	}
	
	public static class Page {
		public static final String TEMPLATE = "page-template.jsp";
		public static final String ERROR = "error.jsp";
		public static final String PRODUCTS = "products.jsp";
		public static final String SHOPPING_CART = "shopping-cart.jsp";
		public static final String SEARCH_RESULT = "search-result.jsp";
		
		private Page() {}
	}
	
	public static class Fragment {
		public static final String PRODUCT_LIST = "product-list.jsp";
		
		private Fragment() {}
	}

	public enum Cookie {
		SHOPPING_CART("iSCC", 60 * 60 * 24 * 365);
		
		private final String name;
		private final int ttl;
		
		private Cookie(String name, int ttl) {
			this.name = name;
			this.ttl = ttl;
		}

		public String getName() {
			return name;
		}

		public int getTtl() {
			return ttl;
		}
	}
	
	private Constants() {
	}
}
