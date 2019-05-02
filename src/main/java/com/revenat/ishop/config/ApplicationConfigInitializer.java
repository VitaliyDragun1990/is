package com.revenat.ishop.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import com.revenat.ishop.filter.CategoryProducerLoaderFilter;
import com.revenat.ishop.filter.ErrorHandlerFilter;
import com.revenat.ishop.filter.HTMLMinifierFilter;
import com.revenat.ishop.filter.ShoppingCartDeserializationFilter;
import com.revenat.ishop.listener.IShopApplicationListener;
import com.revenat.ishop.servlet.ajax.AddProductToShoppingCartController;
import com.revenat.ishop.servlet.ajax.LoadMoreProductsByCategoryController;
import com.revenat.ishop.servlet.ajax.LoadMoreProductsController;
import com.revenat.ishop.servlet.ajax.LoadMoreProductsForSearchResultController;
import com.revenat.ishop.servlet.ajax.RemoveProductFromShoppingCartController;
import com.revenat.ishop.servlet.page.AllProductsController;
import com.revenat.ishop.servlet.page.ErrorController;
import com.revenat.ishop.servlet.page.ProductsByCategoryController;
import com.revenat.ishop.servlet.page.SearchController;
import com.revenat.ishop.servlet.page.ShoppingCartController;

public class ApplicationConfigInitializer implements ServletContainerInitializer {
	//TODO: refactor: creating ServiceManager in onStartup and injecting dep from it into each component
	//TODO: pass ServiceManager instance into ISopApplicationListener to close when destroy
	
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		ServletRegistration.Dynamic servletReg = ctx.addServlet("AllProductsController", new AllProductsController());
		servletReg.addMapping("/products");
		
		servletReg = ctx.addServlet("ErrorController", ErrorController.class);
		servletReg.addMapping("/error");
		
		servletReg = ctx.addServlet("ProductsByCategoryController", new ProductsByCategoryController());
		servletReg.addMapping("/products/*");
		
		servletReg = ctx.addServlet("SearchController", new SearchController());
		servletReg.addMapping("/search");
		
		servletReg = ctx.addServlet("ShoppingCartController", new ShoppingCartController());
		servletReg.addMapping("/shopping-cart");
		
		servletReg = ctx.addServlet("LoadMoreProductsController", new LoadMoreProductsController());
		servletReg.addMapping("/ajax/html/more/products");
		
		servletReg = ctx.addServlet("LoadMoreProductsByCategoryController", new LoadMoreProductsByCategoryController());
		servletReg.addMapping("/ajax/html/more/products/*");
		
		servletReg = ctx.addServlet("LoadMoreProductsForSearchResultController", new LoadMoreProductsForSearchResultController());
		servletReg.addMapping("/ajax/html/more/search");
		
		servletReg = ctx.addServlet("AddProductToShoppingCartController", new AddProductToShoppingCartController());
		servletReg.addMapping("/ajax/json/cart/product/add");
		
		servletReg = ctx.addServlet("RemoveProductFromShoppingCartController", new RemoveProductFromShoppingCartController());
		servletReg.addMapping("/ajax/json/cart/product/remove");
		
		FilterRegistration.Dynamic filterReg = ctx.addFilter("ErrorHandlerFilter", new ErrorHandlerFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				false,
				"/*");
		
		filterReg = ctx.addFilter("HTMLMinifierFilter", HTMLMinifierFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		filterReg = ctx.addFilter("CategoryProducerLoaderFilter", CategoryProducerLoaderFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		ctx.addListener(new IShopApplicationListener());
		
		filterReg = ctx.addFilter("ShoppingCartDeserializationFilter",new ShoppingCartDeserializationFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
	}
}
