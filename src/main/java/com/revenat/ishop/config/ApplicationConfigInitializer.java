package com.revenat.ishop.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.revenat.ishop.config.Constants.URL;
import com.revenat.ishop.filter.AuthenticationFilter;
import com.revenat.ishop.filter.CategoryProducerLoaderFilter;
import com.revenat.ishop.filter.ErrorHandlerFilter;
import com.revenat.ishop.filter.HtmlMinificationFilter;
import com.revenat.ishop.filter.SetCurrentRequestUriFilter;
import com.revenat.ishop.filter.ShoppingCartDeserializationFilter;
import com.revenat.ishop.listener.IShopApplicationListener;
import com.revenat.ishop.servlet.ajax.AddProductToShoppingCartController;
import com.revenat.ishop.servlet.ajax.LoadMoreOrdersController;
import com.revenat.ishop.servlet.ajax.LoadMoreProductsByCategoryController;
import com.revenat.ishop.servlet.ajax.LoadMoreProductsController;
import com.revenat.ishop.servlet.ajax.LoadMoreProductsForSearchResultController;
import com.revenat.ishop.servlet.ajax.RemoveProductFromShoppingCartController;
import com.revenat.ishop.servlet.page.AllProductsController;
import com.revenat.ishop.servlet.page.ErrorController;
import com.revenat.ishop.servlet.page.MyOrdersController;
import com.revenat.ishop.servlet.page.OrderController;
import com.revenat.ishop.servlet.page.ProductsByCategoryController;
import com.revenat.ishop.servlet.page.SearchController;
import com.revenat.ishop.servlet.page.ShoppingCartController;
import com.revenat.ishop.servlet.page.SignInController;
import com.revenat.ishop.servlet.page.SignOutController;
import com.revenat.ishop.servlet.page.SocialLoginController;

public class ApplicationConfigInitializer implements ServletContainerInitializer {
	//TODO: refactor: creating ServiceManager in onStartup and injecting dep from it into each component
	//TODO: pass ServiceManager instance into ISopApplicationListener to close when destroy
	
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		ServletRegistration.Dynamic servletReg = ctx.addServlet("AllProductsController", new AllProductsController());
		servletReg.addMapping(URL.ALL_PRODUCTS);
		
		servletReg = ctx.addServlet("ErrorController", ErrorController.class);
		servletReg.addMapping(URL.ERROR);
		
		servletReg = ctx.addServlet("ProductsByCategoryController", new ProductsByCategoryController());
		servletReg.addMapping(URL.PRODUCTS_BY_CATEGORY);
		
		servletReg = ctx.addServlet("SearchController", new SearchController());
		servletReg.addMapping(URL.SEARCH);
		
		servletReg = ctx.addServlet("ShoppingCartController", new ShoppingCartController());
		servletReg.addMapping(URL.SHOPPING_CART);
		
		servletReg = ctx.addServlet("SignInController", new SignInController());
		servletReg.addMapping(URL.SIGN_IN);
		
		servletReg = ctx.addServlet("SignOutController", new SignOutController());
		servletReg.addMapping(URL.SIGN_OUT);
		
		servletReg = ctx.addServlet("SocialLoginController", new SocialLoginController());
		servletReg.addMapping(URL.SOCIAL_LOGIN);
		
		servletReg = ctx.addServlet("MyOrdersController", new MyOrdersController());
		servletReg.addMapping(URL.MY_ORDERS);
		
		servletReg = ctx.addServlet("OrderController", new OrderController());
		servletReg.addMapping(URL.ORDER);
		
		servletReg = ctx.addServlet("LoadMoreProductsController", new LoadMoreProductsController());
		servletReg.addMapping(URL.AJAX_MORE_PRODUCTS);
		
		servletReg = ctx.addServlet("LoadMoreProductsByCategoryController", new LoadMoreProductsByCategoryController());
		servletReg.addMapping(URL.AJAX_MORE_PRODUCTS_BY_CATEGORY);
		
		servletReg = ctx.addServlet("LoadMoreProductsForSearchResultController", new LoadMoreProductsForSearchResultController());
		servletReg.addMapping(URL.AJAX_MORE_SEARCH);
		
		servletReg = ctx.addServlet("AddProductToShoppingCartController", new AddProductToShoppingCartController());
		servletReg.addMapping(URL.AJAX_ADD_PRODUCT_TO_CART);
		
		servletReg = ctx.addServlet("RemoveProductFromShoppingCartController", new RemoveProductFromShoppingCartController());
		servletReg.addMapping(URL.AJAX_REMOVE_PRODUCT_FROM_CART);
		
		servletReg = ctx.addServlet("LoadMoreOrdersController", new LoadMoreOrdersController());
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
		
		filterReg = ctx.addFilter("CategoryProducerLoaderFilter", CategoryProducerLoaderFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("ShoppingCartDeserializationFilter",new ShoppingCartDeserializationFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("SetCurrentRequestUriFilter",new SetCurrentRequestUriFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = ctx.addFilter("AuthenticationFilter",new AuthenticationFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				URL.MY_ORDERS, URL.ORDER, URL.AJAX_MORE_ORDERS);
		
		ctx.addListener(new IShopApplicationListener());
	}
}
