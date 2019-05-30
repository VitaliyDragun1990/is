package com.revenat.ishop.application.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.application.mapper.ShoppingCartMapper;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.application.service.OrderService;
import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.infrastructure.framework.factory.DependencyInjectionManager;
import com.revenat.ishop.infrastructure.service.PropertiesLoader;

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
	private final DependencyInjectionManager dependencyInjectionManager;

	public OrderService getOrderService() {
		return dependencyInjectionManager.getInstance(OrderService.class);
	}

	public ProductService getProductService() {
		return dependencyInjectionManager.getInstance(ProductService.class);
	}
	
	public CategoryService getCategoryService() {
		return dependencyInjectionManager.getInstance(CategoryService.class);
	}
	
	public ProducerService getProducerService() {
		return dependencyInjectionManager.getInstance(ProducerService.class);
	}

	public ShoppingCartService getShoppingCartService() {
		return dependencyInjectionManager.getInstance(ShoppingCartService.class);
	}

	public String getApplicationProperty(String propertyName) {
		String value =  applicationProperties.getProperty(propertyName);
		if (value.startsWith("${sysEnv.")) {
			value = value.replace("${sysEnv.", "").replace("}", "");
			return System.getProperty(value);
		}
		return value;
	}

	public AuthenticationService getAuthService() {
		return dependencyInjectionManager.getInstance(AuthenticationService.class);
	}
	
	public OrderManager getOrderManager() {
		return dependencyInjectionManager.getInstance(OrderManager.class);
	}
	
	@SuppressWarnings("unchecked")
	public ShoppingCartMapper<String> getCartMapper() {
		return dependencyInjectionManager.getInstance(ShoppingCartMapper.class);
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
		dependencyInjectionManager.destroyInstances();
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
		
		Map<Class<?>, Object> externalDependencies = new HashMap<>();
		externalDependencies.put(DataSource.class, dataSource);
		
		dependencyInjectionManager = new DependencyInjectionManager(applicationProperties, externalDependencies);
		dependencyInjectionManager.scanPackages(
				"com.revenat.ishop.application.mapper.impl",
				"com.revenat.ishop.application.service.impl",
				"com.revenat.ishop.infrastructure.repository",
				"com.revenat.ishop.infrastructure.service.impl"
				);
		dependencyInjectionManager.injectDependencies();
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
