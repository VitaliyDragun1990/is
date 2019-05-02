package com.revenat.ishop.testenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.revenat.ishop.entity.Category;
import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.util.jdbc.JDBCUtils;

public class Main {

	private static final String PASSWORD = "password";
	private static final String USERNAME = "ishop";
	private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/ishop";

	public static void main(String[] args) throws SQLException {
		try (Connection con = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

			List<Category> allCategories = EntityGenerator.getAllCategories();
			for (Category category : allCategories) {
				saveCategory(con, category);
			}
			
			List<Producer> allProducers = EntityGenerator.getAllProducers();
			for (Producer producer : allProducers) {
				saveProducer(con, producer);
			}
			
			List<Product> products = EntityGenerator.generateProducts(1200);
			for (Product product : products) {
				int producerId = findProducerId(product.getProducer(), allProducers);
				int categoryId = findCategoryId(product.getCategory(), allCategories);
				int productId = saveProduct(con, product, categoryId, producerId);
				product.setId(productId);
			}
		}
		System.out.println("Database populated with mock data");
	}
	
	private static int findProducerId(String producerName, List<Producer> allProducers) {
		return allProducers.stream()
				.filter(p -> p.getName().equals(producerName))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("There is not producer with name: " + producerName))
				.getId();
	}

	private static int findCategoryId(String categoryName, List<Category> allCategories) {
		return allCategories.stream()
				.filter(c -> c.getName().equals(categoryName))
				.findFirst().orElseThrow(() -> new IllegalArgumentException("There is no category with name: " + categoryName))
				.getId();
	}

	private static void saveCategory(Connection con, Category cat) throws SQLException {
		JDBCUtils.insert(con,
				"INSERT INTO category (id, name, url) VALUES (?,?,?)",
				rs -> -1,
				cat.getId(), cat.getName(), cat.getUrl());
	}
	
	private static void saveProducer(Connection con, Producer prod) throws SQLException {
		JDBCUtils.insert(con, 
				"INSERT INTO producer (id, name) VALUES (?,?)",
				rs -> -1,
				prod.getId(),
				prod.getName());
	}
	
	private static int saveProduct(Connection con, Product pr, Integer categoryId, Integer producerId) throws SQLException {
		return JDBCUtils.insert(con, "INSERT INTO product (name, description, image_link, price, category_id, producer_id) "
				+ "VALUES (?,?,?,?,?,?)",
				rs -> {
					if (rs.next()) {
						return rs.getInt("id");
					}
					return -1;
				}
				, pr.getName(), pr.getDescription(), pr.getImageLink(), pr.getPrice(), categoryId, producerId);
	}
}
