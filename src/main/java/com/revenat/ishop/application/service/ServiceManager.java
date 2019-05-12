package com.revenat.ishop.application.service;

import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.application.mapper.ShoppingCartMapper;
import com.revenat.ishop.application.mapper.impl.ShoppingCartStringMapper;
import com.revenat.ishop.application.service.impl.FacebookSocialService;
import com.revenat.ishop.application.service.impl.OrderServiceImpl;
import com.revenat.ishop.application.service.impl.ProductServiceImpl;
import com.revenat.ishop.application.service.impl.SocialAuthenticationService;
import com.revenat.ishop.persistence.repository.ProductRepository;
import com.revenat.ishop.persistence.repository.RepositoryFactory;

/**
 * This component exists as single instance and resides in the
 * {@link ServletContext} object. It is responsible for instantiating different
 * application/domain services, providing access to them, and also for
 * gracefully destroying them when application closes.
 * 
 * @author Vitaly Dragun
 *
 */
public class ServiceManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceManager.class);
	public static final String APPLICATION_PROPERTIES = "application.properties";
	private static ServiceManager instance = null;

	private final Properties applicationProperties;
	private final BasicDataSource dataSource;
	private final ShoppingCartMapper<String> cartMapper;
	private final ProductService productService;
	private final OrderService orderService;
	private final ShoppingCartService shoppingCartService;
	private final SocialService socialService;
	private final AuthenticationService authService;
	private final OrderManager orderManager;

	public OrderService getOrderService() {
		return orderService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	public String getApplicationProperty(String propertyName) {
		return applicationProperties.getProperty(propertyName);
	}

	public AuthenticationService getAuthService() {
		return authService;
	}
	
	public OrderManager getOrderManager() {
		return orderManager;
	}
	
	public ShoppingCartMapper<String> getCartMapper() {
		return cartMapper;
	}

	public static synchronized ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return instance;
	}

	public void close() {
		try {
			dataSource.close();
		} catch (SQLException e) {
			LOGGER.error("Error while closing datasource: " + e.getMessage(), e);
		}
	}

	private ServiceManager() {
		applicationProperties = loadApplicationProperties();
		dataSource = createDataSource(
				getApplicationProperty("db.url"),
				getApplicationProperty("db.username"),
				getApplicationProperty("db.password"),
				getApplicationProperty("db.driver"),
				getApplicationProperty("db.pool.initSize"),
				getApplicationProperty("db.pool.maxSize"));

		ProductRepository productRepo = RepositoryFactory.createProductRepository(dataSource);
		productService = new ProductServiceImpl(
				productRepo,
				RepositoryFactory.createCategoryRepository(dataSource),
				RepositoryFactory.createProducerRepository(dataSource));
		orderService = new OrderServiceImpl(
				RepositoryFactory.createOrderRepository(dataSource,  RepositoryFactory.createOrderItemRepository(dataSource)));
		shoppingCartService = new ShoppingCartService(productRepo);
		cartMapper = new ShoppingCartStringMapper(shoppingCartService);
		socialService = new FacebookSocialService(
				getApplicationProperty("social.facebook.appId"),
				getApplicationProperty("social.facebook.secret"),
				getApplicationProperty("app.host") + "/social-login");
		authService = new SocialAuthenticationService(socialService, RepositoryFactory.createAccountRepository(dataSource));
		orderManager = new OrderManager(authService, orderService);
	}

	private Properties loadApplicationProperties() {
		return new PropertiesLoader().load(APPLICATION_PROPERTIES);
	}

	private BasicDataSource createDataSource(String url, String username, String password, String driverClass,
			String poolInitSize, String poolMaxSize) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDefaultAutoCommit(false);
		ds.setRollbackOnReturn(true);
		ds.setDriverClassName(driverClass);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		ds.setInitialSize(Integer.parseInt(poolInitSize));
		ds.setMaxTotal(Integer.parseInt(poolMaxSize));

		return ds;
	}
}
