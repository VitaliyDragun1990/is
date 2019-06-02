package com.revenat.ishop.ui.config;

/**
 * This helper class contains all contant values which are used across all
 * application.
 * 
 * @author Vitaly Dragun
 *
 */
public final class Constants {
	public static final int MAX_PRODUCTS_PER_HTML_PAGE = 12;
	public static final int MAX_ORDERS_PER_HTML_PAGE = 5;
	public static final int SESSION_MAX_INACTIVE_INTERVAL = 30 * 60;

	public static class Attribute {
		public static final String ACCOUNT_REQUEST_STATISTICS = "ACCOUNT_REQUEST_STATISTICS";
		public static final String SERVICE_MANAGER = "SERVICE_MAMANGER";
		public static final String CURRENT_SHOPPING_CART = "CURRENT_SHOPPING_CART";
		public static final String ALL_CATEGORIES = "allCategories";
		public static final String USERS_SESSIONS = "usersSessions";
		public static final String ALL_PRODUCERS = "allProducers";
		public static final String FILTER_CATEGORIES = "filterCategories";
		public static final String FILTER_PRODUCERS = "filterProducers";
		public static final String TOTAL_PAGE_COUNT = "totalPageCount";
		public static final String CURRENT_USER = "CURRENT_USER";
		public static final String PRODUCTS = "products";
		public static final String SELECTED_CATEGORY_URL = "selectedCategoryUrl";
		public static final String PRODUCT_COUNT = "productCount";
		public static final String SEARCH_FORM = "searchForm";
		public static final String REDIRECT_URL = "REDIRECT_URL";
		public static final String REQUEST_URL = "REQUEST_URL";
		public static final String CURRENT_REQUEST_URL = "CURRENT_REQUEST_URL";
		public static final String CURRENT_MESSAGE = "CURRENT_MESSAGE";
		public static final String CURRENT_ORDER = "CURRENT_ORDER";
		public static final String ORDERS = "orders";
		public static final String STATUS_CODE = "statusCode";
		public static final String CLIENT_SESSION = "clientSession";
		public static final String I18N_SERVICE = "i18nService";
		public static final String ERROR_MESSAGE = "errorMessage";
		public static final String REDIRECT_TO_NEW_ORDER = "redirectToNewOrder";

		private Attribute() {
		}
	}

	public static class Directory {
		public static final String JSP = "/WEB-INF/jsp/";
		public static final String FRAGMENT = JSP + "fragment/";
		public static final String PAGE = JSP + "page/";

		private Directory() {
		}
	}

	public static class Page {
		public static final String TEMPLATE = "page-template.jsp";
		public static final String ERROR = "error.jsp";
		public static final String PRODUCTS = "products.jsp";
		public static final String SHOPPING_CART = "shopping-cart.jsp";
		public static final String SEARCH_RESULT = "search-result.jsp";
		public static final String SIGN_IN = "sign-in.jsp";
		public static final String MY_ORDERS = "my-orders.jsp";
		public static final String ORDER = "order.jsp";

		private Page() {
		}
	}

	public static class Fragment {
		public static final String PRODUCT_LIST = "product-list.jsp";
		public static final String ORDER_LIST = "order-list.jsp";

		private Fragment() {
		}
	}

	public static class URL {
		public static final String AJAX_MORE_PRODUCTS = "/ajax/html/more/products";
		public static final String AJAX_MORE_PRODUCTS_BY_CATEGORY = "/ajax/html/more/products/*";
		public static final String AJAX_MORE_SEARCH = "/ajax/html/more/search";
		public static final String AJAX_ADD_PRODUCT_TO_CART = "/ajax/json/cart/product/add";
		public static final String AJAX_REMOVE_PRODUCT_FROM_CART = "/ajax/json/cart/product/remove";
		public static final String AJAX_MORE_ORDERS = "/ajax/html/more/my-orders";
		public static final String ALL_PRODUCTS = "/products";
		public static final String PRODUCTS_BY_CATEGORY = "/products/*";
		public static final String MY_ORDERS = "/my-orders";
		public static final String ORDER = "/order";
		public static final String SIGN_IN = "/sign-in";
		public static final String SIGN_OUT = "/sign-out";
		public static final String ERROR = "/error";
		public static final String SEARCH = "/search";
		public static final String SHOPPING_CART = "/shopping-cart";
		public static final String SOCIAL_LOGIN = "/social-login";
		public static final String ORDER_WITH_ID = "/order?id=";

		private URL() {
		}
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
