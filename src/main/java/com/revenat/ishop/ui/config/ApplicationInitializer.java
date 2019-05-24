package com.revenat.ishop.ui.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.revenat.ishop.application.service.impl.ServiceManager;
import com.revenat.ishop.ui.config.Constants.URL;
import com.revenat.ishop.ui.controller.ajax.AddProductToShoppingCartController;
import com.revenat.ishop.ui.controller.ajax.LoadMoreAllProductsController;
import com.revenat.ishop.ui.controller.ajax.LoadMoreOrdersController;
import com.revenat.ishop.ui.controller.ajax.LoadMoreProductsByCategoryController;
import com.revenat.ishop.ui.controller.ajax.LoadMoreProductsForSearchResultController;
import com.revenat.ishop.ui.controller.ajax.RemoveProductFromShoppingCartController;
import com.revenat.ishop.ui.controller.page.AllProductsController;
import com.revenat.ishop.ui.controller.page.ErrorController;
import com.revenat.ishop.ui.controller.page.MyOrdersController;
import com.revenat.ishop.ui.controller.page.OrderController;
import com.revenat.ishop.ui.controller.page.ProductSearchController;
import com.revenat.ishop.ui.controller.page.ProductsByCategoryController;
import com.revenat.ishop.ui.controller.page.ShoppingCartController;
import com.revenat.ishop.ui.controller.page.SignInController;
import com.revenat.ishop.ui.controller.page.SignOutController;
import com.revenat.ishop.ui.controller.page.SocialLoginController;
import com.revenat.ishop.ui.filter.AuthenticationFilter;
import com.revenat.ishop.ui.filter.CategoriesAndProducersLoaderFilter;
import com.revenat.ishop.ui.filter.ErrorHandlerFilter;
import com.revenat.ishop.ui.filter.HtmlMinificationFilter;
import com.revenat.ishop.ui.filter.RequestUrlMemorizerFilter;
import com.revenat.ishop.ui.filter.ShoppingCartDeserializationFilter;
import com.revenat.ishop.ui.listener.ApplicationInitializationListener;
import com.revenat.ishop.ui.service.ShoppingCartCookieSerializer;

public class ApplicationInitializer implements ServletContainerInitializer {
	
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		ServiceManager serviceManager = ServiceManager.getInstance(ctx.getRealPath("/"));
		
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
