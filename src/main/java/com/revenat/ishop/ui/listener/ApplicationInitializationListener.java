package com.revenat.ishop.ui.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.application.service.impl.ServiceManager;
import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.ui.config.Constants.Attribute;

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
public class ApplicationInitializationListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInitializationListener.class);

	private final ServiceManager serviceManager;
	
	public ApplicationInitializationListener(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ServletContext servletContext = sce.getServletContext();
			setInitAttributes(servletContext);
		} catch (RuntimeException e) {
			LOGGER.error("Web application 'ishop' initialization failed: " + e.getMessage(), e);
			throw e;
		}
		LOGGER.info("Web application 'ishop' initialized.");
	}

	private void setInitAttributes(ServletContext servletContext) {
		List<Category> categories = serviceManager.getCategoryService().findAllCategories();
		servletContext.setAttribute(Attribute.ALL_CATEGORIES, categories);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		serviceManager.close();
		LOGGER.info("Web application 'ishop' destroyed.");
	}
}
