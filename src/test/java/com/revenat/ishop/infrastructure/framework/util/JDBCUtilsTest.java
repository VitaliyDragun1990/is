package com.revenat.ishop.infrastructure.framework.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.revenat.ishop.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.ishop.infrastructure.framework.util.JDBCUtils;
import com.revenat.ishop.infrastructure.framework.util.JDBCUtils.ResultSetHandler;

public class JDBCUtilsTest {
	private static final String DELETE_ALL = "DELETE FROM product";
	private static final String UPDATE_WITHOUT_PARAMS = "UPDATE product SET name='Macbook Pro' WHERE product_id=1";
	private static final String UPDATE_WITH_PARAMS = "UPDATE product SET name=? WHERE product_id=?";
	private static final String SELECT_BY_ID = "SELECT * FROM product where product_id = ?";
	private static final String JDBC_URL = "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:script/create-test.sql'\\;RUNSCRIPT FROM 'classpath:script/populate-test.sql'";
	private static final String SELECT_ALL = "SELECT * FROM product";
	private static final String INSERT_NEW_WITH_PARAMS = "INSERT INTO product (name, category, producer) VALUES (?,?,?)";
	private static final String INSERT_NEW_WITHOUT_PARAMS =
			"INSERT INTO product (name, category, producer) VALUES ('IPad','Tablet','Apple')";
	private static final String INSERT_BATCH_WITH_PARAMS =
			"INSERT INTO product (product_id, name, category, producer) VALUES (?,?,?,?)";

	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	@Test
	public void shouldNotAllowToSelectWithNullConnection() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Connection can not be null"));
		
		JDBCUtils.select(null, JDBC_URL, new ProductsHandler());
	}
	
	@Test
	public void shouldNotAllowToSelectWithNullSqlQuery() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Sql string can not be null"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.select(c, null, new ProductsHandler());

		}
	}
	
	@Test
	public void shouldNotAllowToSelectWithNullResultSetHandler() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Result set handler can not be null"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.select(c, SELECT_ALL, null);

		}
	}
	
	@Test
	public void shouldNotAllowToSelectWithParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			Product product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);

			assertThat(product.getId(), equalTo(1));
		}
	}
	
	@Test
	public void shouldAllowToSelectWithoutParameters() throws SQLException {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			List<Product> allProducts = JDBCUtils.select(c, SELECT_ALL, new ProductsHandler());

			assertThat(allProducts, hasSize(2));
		}
	}

	@Test
	public void shouldNotAllowToInsertWithNullConnection() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Connection can not be null"));
		
		JDBCUtils.insert(null, INSERT_NEW_WITHOUT_PARAMS, new ProductIdHandler());
	}
	
	@Test
	public void shouldNotAllowToInsertWithNullSqlQuery() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Sql string can not be null"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.insert(c, null, new ProductIdHandler());

		}
	}
	
	@Test
	public void shouldNotAllowToInsertWithNullResultSetHandler() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Result set handler can not be null"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.insert(c, INSERT_NEW_WITHOUT_PARAMS, null);

		}
	}
	
	@Test
	public void shouldAllowToInsertWithParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			int productId = JDBCUtils.insert(c, INSERT_NEW_WITH_PARAMS, new ProductIdHandler(), "IPad", "Tablet", "Apple");
			
			Product product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), productId);
			
			assertThat(product.getId(), equalTo(productId));
			assertThat(product.getName(), equalTo("IPad"));
			assertThat(product.getCategory(), equalTo("Tablet"));
			assertThat(product.getProducer(), equalTo("Apple"));
		}
	}
	
	@Test
	public void shouldAllowToInsertWithoutParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			int productId = JDBCUtils.insert(c, INSERT_NEW_WITHOUT_PARAMS, new ProductIdHandler());
			
			Product product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), productId);
			
			assertThat(product.getId(), equalTo(productId));
			assertThat(product.getName(), equalTo("IPad"));
			assertThat(product.getCategory(), equalTo("Tablet"));
			assertThat(product.getProducer(), equalTo("Apple"));
		}
	}
	
	@Test
	public void shouldNotAllowToUpdateWithNullConnection() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Connection can not be null"));
		
		JDBCUtils.executeUpdate(null, INSERT_NEW_WITHOUT_PARAMS);
	}
	
	@Test
	public void shouldNotAllowToUpdateWithNullSqlQuery() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Sql string can not be null"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.executeUpdate(c, null);
		}
	}
	
	@Test
	public void shouldAllowToUpdateWithParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			Product product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);
			assertThat(product.getName(), equalTo("Macbook Air"));
			
			int rowsUpdated = JDBCUtils.executeUpdate(c, UPDATE_WITH_PARAMS, "Macbook Pro", 1);
			assertThat(rowsUpdated, equalTo(1));
			
			product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);
			assertThat(product.getName(), equalTo("Macbook Pro"));
		}
	}
	
	@Test
	public void shouldAllowToUpdateWithoutParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			Product product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);
			assertThat(product.getName(), equalTo("Macbook Air"));
			
			int rowsUpdated = JDBCUtils.executeUpdate(c, UPDATE_WITHOUT_PARAMS);
			assertThat(rowsUpdated, equalTo(1));
			
			product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);
			assertThat(product.getName(), equalTo("Macbook Pro"));
		}
	}
	
	@Test
	public void shouldAllowToDeleteWithoutParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			List<Product> products = JDBCUtils.select(c, SELECT_ALL, new ProductsHandler());
			assertThat(products, hasSize(2));
			
			int rowsDeleted = JDBCUtils.executeUpdate(c, DELETE_ALL);
			assertThat(rowsDeleted, equalTo(2));
			
			products = JDBCUtils.select(c, SELECT_ALL, new ProductsHandler());
			assertThat(products, empty());
		}
	}
	
	@Test
	public void shouldAllowToDeleteWithParameters() throws Exception {
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			Product product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);
			assertThat(product, notNullValue());
			
			int rowsDeleted = JDBCUtils.executeUpdate(c, "DELETE FROM product WHERE product_id=?", 1);
			assertThat(rowsDeleted, equalTo(1));
			
			product = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1);
			assertNull(product);
		}
	}
	
	@Test
	public void shouldNotAllowToInsertInBatchWithNullSqlQuery() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Sql string can not be null"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.insertBatch(c, null, Collections.emptyList());
		}
	}
	
	@Test
	public void shouldNotAllowToInsertInBatchWithNullConnection() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Connection can not be null"));
		
		JDBCUtils.insertBatch(null, INSERT_NEW_WITHOUT_PARAMS, Collections.emptyList());
	}
	
	@Test
	public void shouldNotAllowToInsertInBatchWithNullParametersList() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Can not execute insertBatch with null or empty parameter list"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.insertBatch(c, INSERT_NEW_WITHOUT_PARAMS, null);
		}
	}
	
	@Test
	public void shouldNotAllowToInsertInBatchWithoutParameters() throws Exception {
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString("Can not execute insertBatch with null or empty parameter list"));
		
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.insertBatch(c, INSERT_NEW_WITHOUT_PARAMS, Collections.emptyList());
		}
	}
	
	@Test
	public void shouldAllowToInsertInBatch() throws Exception {
		List<Object[]> params = Arrays.asList(
				new Object[] {999, "IPad+", "Tablet", "Apple"},
				new Object[] {1999, "IPhone+", "Smartphone", "Apple"}
				);
		try (Connection c = DriverManager.getConnection(JDBC_URL)) {
			JDBCUtils.insertBatch(c, INSERT_BATCH_WITH_PARAMS, params);
			
			Product productA = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 999);
			Product productB = JDBCUtils.select(c, SELECT_BY_ID, new ProductHandler(), 1999);
			
			assertThat(productA.getId(), equalTo(999));
			assertThat(productA.getName(), equalTo("IPad+"));
			assertThat(productA.getCategory(), equalTo("Tablet"));
			assertThat(productA.getProducer(), equalTo("Apple"));
			
			assertThat(productB.getId(), equalTo(1999));
			assertThat(productB.getName(), equalTo("IPhone+"));
			assertThat(productB.getCategory(), equalTo("Smartphone"));
			assertThat(productB.getProducer(), equalTo("Apple"));
		}
	}

	private static class Product {
		private int id;
		private String name;
		private String category;
		private String producer;

		public Product(int id, String name, String category, String producer) {
			this.id = id;
			this.name = name;
			this.category = category;
			this.producer = producer;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getCategory() {
			return category;
		}

		public String getProducer() {
			return producer;
		}

		@Override
		public String toString() {
			return String.format("Product [id=%s, name=%s, category=%s, producer=%s]", id, name, category, producer);
		}
	}

	private static class ProductsHandler implements ResultSetHandler<List<Product>> {
		@Override
		public List<Product> handle(ResultSet rs) throws SQLException {
			List<Product> products = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("product_id");
				String name = rs.getString("name");
				String category = rs.getString("category");
				String producer = rs.getString("producer");
				products.add(new Product(id, name, category, producer));
			}
			return Collections.unmodifiableList(products);
		}

	}
	
	private static class ProductIdHandler implements ResultSetHandler<Integer> {
		@Override
		public Integer handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				return rs.getInt("product_id");
			}
			return -1;
		}

	}

	private static class ProductHandler implements ResultSetHandler<Product> {
		@Override
		public Product handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				int id = rs.getInt("product_id");
				String name = rs.getString("name");
				String category = rs.getString("category");
				String producer = rs.getString("producer");
				return new Product(id, name, category, producer);
			}
			return null;
		}

	}

}
