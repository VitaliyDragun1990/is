package com.revenat.ishop.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SimpleServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("my-message", "Hello");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// ignore
	}
}
