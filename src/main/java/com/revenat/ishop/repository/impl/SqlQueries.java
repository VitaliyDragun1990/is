package com.revenat.ishop.repository.impl;

class SqlQueries {
	public static final String GET_ALL_CATEGORIES = "SELECT * FROM category ORDER BY name";
	public static final String GET_CATEGORIES_BY_CRITERIA_TEMPLATE = "SELECT c.id, c.name, c.url, count(prod.id) AS product_count "
			+ "FROM category AS c LEFT OUTER JOIN "
			+ "(SELECT p.id, p.category_id FROM product AS p INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "%s) "
			+ "AS prod ON c.id = prod.category_id "
			+ "GROUP BY c.id, c.name, c.url "
			+ "ORDER BY c.name";
	public static final String GET_ALL_PRODUCERS = "SELECT * FROM producer ORDER BY name";
	public static final String GET_PRODUCERS_BY_CRITERIA_TEMPLATE = "SELECT pr.id, pr.name, count(prod.id) AS product_count "
			+ "FROM producer AS pr LEFT OUTER JOIN "
			+ "(SELECT p.id, p.producer_id FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "%s) "
			+ "AS prod ON pr.id = prod.producer_id "
			+ "GROUP BY pr.id, pr.name "
			+ "ORDER BY pr.name";
	public static final String GET_PRODUCT_BY_ID = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "WHERE p.id = ? ORDER BY p.id";
	public static final String GET_ALL_PRODUCTS = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	public static final String GET_PRODUCTS_BY_CATEGORY = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id " + "WHERE c.url = ? "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	public static final String GET_PRODUCTS_BY_CRITERIA_TEMPLATE = "SELECT p.*, c.name AS category, pr.name AS producer "
			+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
			+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
			+ " %s "
			+ "ORDER BY p.id LIMIT ? OFFSET ?";
	public static final String GET_ACCOUNT_BY_EMAIL = "SELECT * FROM account WHERE email = ?";
	public static final String COUNT_ALL_PRODUCTS = "SELECT count(*) as count FROM product";
	public static final String COUNT_PRODUCTS_BY_CATEGORY = "SELECT product_count AS count FROM category AS c "
			+ "WHERE c.url = ?";
	public static final String COUNT_PRODUCTS_BY_CRITERIA_TEMPLATE = "SELECT count(*) AS count "
				+ "FROM product AS p INNER JOIN category AS c ON p.category_id = c.id "
				+ "INNER JOIN producer AS pr ON p.producer_id = pr.id "
				+ "%s";
	public static final String INSERT_ACCOUNT = "INSERT INTO account (name, email) "
			+ "VALUES (?,?)";
	
	private SqlQueries() {}
}
