package com.revenat.ishop.service;

import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.config.PropertiesLoader;
import com.revenat.ishop.repository.AccountRepository;
import com.revenat.ishop.repository.CategoryRepository;
import com.revenat.ishop.repository.OrderItemRepository;
import com.revenat.ishop.repository.OrderRepository;
import com.revenat.ishop.repository.ProducerRepository;
import com.revenat.ishop.repository.ProductRepository;
import com.revenat.ishop.repository.ShoppingCartRepository;
import com.revenat.ishop.repository.impl.JdbcAccountRepository;
import com.revenat.ishop.repository.impl.JdbcCategoryRepository;
import com.revenat.ishop.repository.impl.JdbcOrderItemRepository;
import com.revenat.ishop.repository.impl.JdbcOrderRepository;
import com.revenat.ishop.repository.impl.JdbcProducerRepository;
import com.revenat.ishop.repository.impl.JdbcProductRepository;
import com.revenat.ishop.service.application.AuthenticationService;
import com.revenat.ishop.service.application.OrderManager;
import com.revenat.ishop.service.application.ShoppingCartService;
import com.revenat.ishop.service.application.SocialService;
import com.revenat.ishop.service.application.impl.FacebookSocialService;
import com.revenat.ishop.service.application.impl.ShoppingCartCookieStringMapper;
import com.revenat.ishop.service.application.impl.SocialAuthenticationService;
import com.revenat.ishop.service.domain.OrderService;
import com.revenat.ishop.service.domain.ProductService;
import com.revenat.ishop.service.domain.impl.OrderServiceImpl;
import com.revenat.ishop.service.domain.impl.ProductServiceImpl;

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

	private final Properties applicationProperties;
	private final BasicDataSource dataSource;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final ProducerRepository producerRepository;
	private final ShoppingCartRepository shoppingCartRepository;
	private final AccountRepository accountRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final ProductService productService;
	private final OrderService orderService;
	private final ShoppingCartService shoppingCartService;
	private final SocialService socialService;
	private final AuthenticationService authService;
	private final OrderManager orderManager;

	public ShoppingCartRepository getShoppingCartRepository() {
		return shoppingCartRepository;
	}

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

	public SocialService getSocialService() {
		return socialService;
	}

	public AuthenticationService getAuthService() {
		return authService;
	}
	
	public OrderManager getOrderManager() {
		return orderManager;
	}

	public static synchronized ServiceManager getInstance(ServletContext context) {
		ServiceManager instance = (ServiceManager) context.getAttribute(Attribute.SERVICE_MANAGER);
		if (instance == null) {
			instance = new ServiceManager(context);
			context.setAttribute(Attribute.SERVICE_MANAGER, instance);
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

	private ServiceManager(ServletContext context) {
		applicationProperties = loadApplicationProperties();
		dataSource = createDataSource(getApplicationProperty("db.url"), getApplicationProperty("db.username"),
				getApplicationProperty("db.password"), getApplicationProperty("db.driver"),
				getApplicationProperty("db.pool.initSize"), getApplicationProperty("db.pool.maxSize"));
		productRepository = new JdbcProductRepository(dataSource);
		categoryRepository = new JdbcCategoryRepository(dataSource);
		producerRepository = new JdbcProducerRepository(dataSource);
		orderItemRepository = new JdbcOrderItemRepository(dataSource);
		orderRepository = new JdbcOrderRepository(dataSource, orderItemRepository);
		shoppingCartRepository = new ShoppingCartRepository(new ShoppingCartCookieStringMapper(productRepository));
		accountRepository = new JdbcAccountRepository(dataSource);
		productService = new ProductServiceImpl(productRepository, categoryRepository, producerRepository);
		orderService = new OrderServiceImpl(orderRepository);
		shoppingCartService = new ShoppingCartService(shoppingCartRepository, productRepository);
		socialService = new FacebookSocialService(getApplicationProperty("social.facebook.appId"),
				getApplicationProperty("social.facebook.secret"), getApplicationProperty("app.host") + "/social-login");
		authService = new SocialAuthenticationService(socialService, accountRepository);
		orderManager = new OrderManager(shoppingCartService, authService, orderService);
	}

	private Properties loadApplicationProperties() {
		return new PropertiesLoader().load(Constants.APPLICATION_PROPERTIES);
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
