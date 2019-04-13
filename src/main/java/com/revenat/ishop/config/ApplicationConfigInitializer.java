package com.revenat.ishop.config;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.revenat.ishop.servlet.AdminServlet;
import com.revenat.ishop.servlet.GetParametersServlet;
import com.revenat.ishop.servlet.JavaConfigServlet;

public class ApplicationConfigInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		JavaConfigServlet servlet = new JavaConfigServlet();
		ServletRegistration.Dynamic registration = ctx.addServlet("JavaConfigServlet", servlet);
		registration.addMapping("/java");
		
		registration = ctx.addServlet("GetParametersServlet", GetParametersServlet.class);
		registration.addMapping("/params");
		
		registration = ctx.addServlet("AdminServlet", AdminServlet.class);
		registration.addMapping("/admin");
		registration.setInitParameter("ip", "127.0.0.1");
		registration.setInitParameter("accessKey", "super-secret-access-key");
		registration.setInitParameter("login", "admin");
		registration.setInitParameter("password", "password");
	}
}
