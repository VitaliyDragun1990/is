package com.revenat.ishop.application.service.impl;

import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.application.mapper.ShoppingCartMapper;
import com.revenat.ishop.application.mapper.impl.ShoppingCartStringMapper;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.infrastructure.repository.ProductRepository;
import com.revenat.ishop.infrastructure.repository.RepositoryFactory;
import com.revenat.ishop.infrastructure.service.NotificationService;
import com.revenat.ishop.infrastructure.service.SocialService;
import com.revenat.ishop.infrastructure.service.impl.ServiceFactory;

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
	private final CategoryService categoryService;
	private final ProducerService producerService;
	private final OrderService orderService;
	private final ShoppingCartService shoppingCartService;
	private final SocialService socialService;
	private final AuthenticationService authService;
	private final NotificationService notificationService;
	private final OrderManager orderManager;

	public OrderService getOrderService() {
		return orderService;
	}

	public ProductService getProductService() {
		return productService;
	}
	
	public CategoryService getCategoryService() {
		return categoryService;
	}
	
	public ProducerService getProducerService() {
		return producerService;
	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	public String getApplicationProperty(String propertyName) {
		String value =  applicationProperties.getProperty(propertyName);
		if (value.startsWith("${sysEnv.")) {
			value = value.replace("${sysEnv.", "").replace("}", "");
			return System.getenv(value);
		}
		return value;
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

	public static synchronized ServiceManager getInstance(String applicationRootDir) {
		if (instance == null) {
			instance = new ServiceManager(applicationRootDir);
		}
		return instance;
	}

	public void close() {
		try {
			dataSource.close();
			notificationService.shutdown();
		} catch (SQLException e) {
			LOGGER.error("Error while closing datasource: " + e.getMessage(), e);
		}
	}

	private ServiceManager(String applicationRootDir) {
		applicationProperties = loadApplicationProperties();
		dataSource = createDataSource(
				getApplicationProperty("db.url"),
				getApplicationProperty("db.username"),
				getApplicationProperty("db.password"),
				getApplicationProperty("db.driver"),
				getApplicationProperty("db.pool.initSize"),
				getApplicationProperty("db.pool.maxSize"));

		ProductRepository productRepo = RepositoryFactory.createProductRepository(dataSource);
		categoryService = new CategoryServiceImpl(RepositoryFactory.createCategoryRepository(dataSource));
		producerService = new ProducerServiceImpl(RepositoryFactory.createProducerRepository(dataSource));
		productService = new ProductServiceImpl(productRepo);
		orderService = new OrderServiceImpl(
				RepositoryFactory.createOrderRepository(dataSource,  RepositoryFactory.createOrderItemRepository(dataSource)));
		shoppingCartService = new ShoppingCartService(productRepo);
		cartMapper = new ShoppingCartStringMapper(shoppingCartService);
		socialService = ServiceFactory.createSocialSevice(
				getApplicationProperty("social.facebook.appId"),
				getApplicationProperty("social.facebook.secret"),
				getApplicationProperty("app.host") + "/social-login");
		authService = new SocialAuthenticationService(socialService,
				ServiceFactory.createAvatarService(applicationRootDir),
				RepositoryFactory.createAccountRepository(dataSource));
		notificationService = ServiceFactory.createNotificationService(getEmailProperties());
		orderManager = new OrderManager(
				authService,
				orderService,
				new FeedbackServiceImpl(notificationService, getApplicationProperty("app.host"))
				);
	}
	
	private Properties getEmailProperties() {
		Properties emailProps = new Properties();
		emailProps.setProperty("email.fromEmail", getApplicationProperty("email.fromEmail"));
		emailProps.setProperty("email.sendTryAttempts", getApplicationProperty("email.sendTryAttempts"));
		emailProps.setProperty("email.smtp.server", getApplicationProperty("email.smtp.server"));
		emailProps.setProperty("email.smtp.port", getApplicationProperty("email.smtp.port"));
		emailProps.setProperty("email.smtp.username", getApplicationProperty("email.smtp.username"));
		emailProps.setProperty("email.smtp.password", getApplicationProperty("email.smtp.password"));
		return emailProps;
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
