package com.revenat.ishop.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.config.Constants.Attribute;
import com.revenat.ishop.entity.Category;
import com.revenat.ishop.service.impl.ServiceManager;

/**
 * This implementation of the {@link ServletContextListener} is responsible for
 * initializing {@link ServiceManager} instance on {@code condextInitialized}
 * event and for destroying it on {@code contextDestroyed} event. It is also
 * responsible for saving to {@link ServletContext} lists with all
 * {@link Category} entities available at the time of
 * applciation starts. This approach is suitable when we do not insert/delete
 * these entities when the application is up and running thus
 * they stay static and may be changed only before starting the application.
 * This approach frees us from frequently queriyng datastore, because we only
 * need to make single request when the application is starting.
 * 
 * @author Vitaly Dragun
 *
 */
public class IShopApplicationListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(IShopApplicationListener.class);

	private ServiceManager serviceManager;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ServletContext servletContext = sce.getServletContext();
			serviceManager = ServiceManager.getInstance(servletContext);
			setInitAttributes(servletContext);
		} catch (RuntimeException e) {
			LOGGER.error("Web application 'ishop' initialization failed: " + e.getMessage(), e);
			throw e;
		}
		LOGGER.info("Web application 'ishop' initialized.");
	}

	private void setInitAttributes(ServletContext servletContext) {
		List<Category> categories = serviceManager.getProductService().getAllCategories();
		servletContext.setAttribute(Attribute.ALL_CATEGORIES, categories);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		serviceManager.close();
		LOGGER.info("Web application 'ishop' destroyed.");
	}
}
