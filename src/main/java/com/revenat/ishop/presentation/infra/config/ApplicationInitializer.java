package com.revenat.ishop.presentation.infra.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.revenat.ishop.application.service.impl.ServiceManager;
import com.revenat.ishop.presentation.controller.ajax.AddProductToShoppingCartController;
import com.revenat.ishop.presentation.controller.ajax.LoadMoreAllProductsController;
import com.revenat.ishop.presentation.controller.ajax.LoadMoreOrdersController;
import com.revenat.ishop.presentation.controller.ajax.LoadMoreProductsByCategoryController;
import com.revenat.ishop.presentation.controller.ajax.LoadMoreProductsForSearchResultController;
import com.revenat.ishop.presentation.controller.ajax.RemoveProductFromShoppingCartController;
import com.revenat.ishop.presentation.controller.page.AllProductsController;
import com.revenat.ishop.presentation.controller.page.ErrorController;
import com.revenat.ishop.presentation.controller.page.MyOrdersController;
import com.revenat.ishop.presentation.controller.page.OrderController;
import com.revenat.ishop.presentation.controller.page.ProductSearchController;
import com.revenat.ishop.presentation.controller.page.ProductsByCategoryController;
import com.revenat.ishop.presentation.controller.page.ShoppingCartController;
import com.revenat.ishop.presentation.controller.page.SignInController;
import com.revenat.ishop.presentation.controller.page.SignOutController;
import com.revenat.ishop.presentation.controller.page.SocialLoginController;
import com.revenat.ishop.presentation.filter.AuthenticationFilter;
import com.revenat.ishop.presentation.filter.CategoriesAndProducersLoaderFilter;
import com.revenat.ishop.presentation.filter.ErrorHandlerFilter;
import com.revenat.ishop.presentation.filter.HtmlMinificationFilter;
import com.revenat.ishop.presentation.filter.RequestUrlMemorizerFilter;
import com.revenat.ishop.presentation.filter.ShoppingCartDeserializationFilter;
import com.revenat.ishop.presentation.infra.config.Constants.URL;
import com.revenat.ishop.presentation.listener.ApplicationInitializationListener;
import com.revenat.ishop.presentation.service.ShoppingCartCookieSerializer;

public class ApplicationInitializer implements ServletContainerInitializer {
	
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		ServiceManager serviceManager = ServiceManager.getInstance();
		
		ServletRegistration.Dynamic servletReg = ctx.addServlet("AllProductsController",
				new AllProductsController(serviceManager.getProductService()));
		servletReg.addMapping(URL.ALL_PRODUCTS);
		
		servletReg = ctx.addServlet("ErrorController", ErrorController.class);
		servletReg.addMapping(URL.ERROR);
		
		servletReg = ctx.addServlet("ProductsByCategoryController",
				new ProductsByCategoryController(serviceManager.getProductService()));
		servletReg.addMapping(URL.PRODUCTS_BY_CATEGORY);
		
		servletReg = ctx.addServlet("SearchController",
				new ProductSearchController(serviceManager.getProductService()));
		servletReg.addMapping(URL.SEARCH);
		
		servletReg = ctx.addServlet("ShoppingCartController", new ShoppingCartController());
		servletReg.addMapping(URL.SHOPPING_CART);
		
		servletReg = ctx.addServlet("SignInController", new SignInController(serviceManager.getAuthService()));
		servletReg.addMapping(URL.SIGN_IN);
		
		servletReg = ctx.addServlet("SignOutController", new SignOutController(serviceManager.getAuthService()));
		servletReg.addMapping(URL.SIGN_OUT);
		
		servletReg = ctx.addServlet("SocialLoginController", new SocialLoginController(serviceManager.getAuthService()));
		servletReg.addMapping(URL.SOCIAL_LOGIN);
		
		servletReg = ctx.addServlet("MyOrdersController", new MyOrdersController(serviceManager.getOrderManager()));
		servletReg.addMapping(URL.MY_ORDERS);
		
		servletReg = ctx.addServlet("OrderController", new OrderController(serviceManager.getOrderManager()));
		servletReg.addMapping(URL.ORDER);
		
		servletReg = ctx.addServlet("LoadMoreAllProductsController",
				new LoadMoreAllProductsController(serviceManager.getProductService()));
		servletReg.addMapping(URL.AJAX_MORE_PRODUCTS);
		
		servletReg = ctx.addServlet("LoadMoreProductsByCategoryController",
				new LoadMoreProductsByCategoryController(serviceManager.getProductService()));
		servletReg.addMapping(URL.AJAX_MORE_PRODUCTS_BY_CATEGORY);
		
		servletReg = ctx.addServlet("LoadMoreProductsForSearchResultController",
				new LoadMoreProductsForSearchResultController(serviceManager.getProductService()));
		servletReg.addMapping(URL.AJAX_MORE_SEARCH);
		
		servletReg = ctx.addServlet("AddProductToShoppingCartController",
				new AddProductToShoppingCartController(serviceManager.getShoppingCartService()));
		servletReg.addMapping(URL.AJAX_ADD_PRODUCT_TO_CART);
		
		servletReg = ctx.addServlet("RemoveProductFromShoppingCartController",
				new RemoveProductFromShoppingCartController(serviceManager.getShoppingCartService()));
		servletReg.addMapping(URL.AJAX_REMOVE_PRODUCT_FROM_CART);
		
		servletReg = ctx.addServlet("LoadMoreOrdersController",
				new LoadMoreOrdersController(serviceManager.getOrderManager()));
		servletReg.addMapping(URL.AJAX_MORE_ORDERS);
		
		FilterRegistration.Dynamic filterReg = ctx.addFilter("ErrorHandlerFilter", new ErrorHandlerFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				false,
				"/*");
		
		filterReg = ctx.addFilter("HtmlMinificationFilter", HtmlMinificationFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("CategoriesAndProducersLoaderFilter",
				new CategoriesAndProducersLoaderFilter(serviceManager.getCategoryService(), serviceManager.getProducerService()));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("ShoppingCartDeserializationFilter",new ShoppingCartDeserializationFilter(
				new ShoppingCartCookieSerializer(serviceManager.getCartMapper())));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("SetCurrentRequestUriFilter",new RequestUrlMemorizerFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("AuthenticationFilter",new AuthenticationFilter(serviceManager.getAuthService()));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				URL.MY_ORDERS, URL.ORDER, URL.AJAX_MORE_ORDERS);
		
		ctx.addListener(new ApplicationInitializationListener(serviceManager));
	}
}
