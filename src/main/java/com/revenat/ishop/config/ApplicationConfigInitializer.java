package com.revenat.ishop.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.revenat.ishop.filter.AuthenticationFilter;
import com.revenat.ishop.filter.HTMLMinifierFilter;
import com.revenat.ishop.filter.ShoppingCartDeserializationFilter;
import com.revenat.ishop.servlet.AdminServlet;
import com.revenat.ishop.servlet.GetParametersServlet;
import com.revenat.ishop.servlet.JavaConfigServlet;
import com.revenat.ishop.servlet.ShoppingCartServlet;
import com.revenat.ishop.util.ShoppingCartCookieMapper;

public class ApplicationConfigInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		JavaConfigServlet servlet = new JavaConfigServlet();
		ServletRegistration.Dynamic servletReg = ctx.addServlet("JavaConfigServlet", servlet);
		servletReg.addMapping("/java");
		
		servletReg = ctx.addServlet("GetParametersServlet", GetParametersServlet.class);
		servletReg.addMapping("/params");
		
		servletReg = ctx.addServlet("AdminServlet", AdminServlet.class);
		servletReg.addMapping("/admin");
		servletReg.setInitParameter("ip", "127.0.0.1");
		servletReg.setInitParameter("accessKey", "super-secret-access-key");
		servletReg.setInitParameter("login", "admin");
		servletReg.setInitParameter("password", "password");
		
		servletReg = ctx.addServlet("ShoppingCartServlet", new ShoppingCartServlet(new ShoppingCartCookieMapper()));
		servletReg.addMapping("/current-cart");
		
		Dynamic filterReg = ctx.addFilter("HTMLMinifierFilter", HTMLMinifierFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				false,
				"/index.jsp");
		filterReg = ctx.addFilter("ShoppingCartDeserializationFilter",
				new ShoppingCartDeserializationFilter(new ShoppingCartCookieMapper()));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		filterReg = ctx.addFilter("AuthenticationFilter", AuthenticationFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/my-secret-page");
	}
}
