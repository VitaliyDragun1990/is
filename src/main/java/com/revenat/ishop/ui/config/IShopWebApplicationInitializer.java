package com.revenat.ishop.ui.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.revenat.ishop.application.mapper.ShoppingCartMapper;
import com.revenat.ishop.application.service.AuthenticationService;
import com.revenat.ishop.application.service.CategoryService;
import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.application.service.OrderManager;
import com.revenat.ishop.application.service.ProducerService;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.ui.config.Constants.Attribute;
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
import com.revenat.ishop.ui.filter.ClientLocaleDefinerFilter;
import com.revenat.ishop.ui.filter.ErrorHandlerFilter;
import com.revenat.ishop.ui.filter.HtmlMinificationFilter;
import com.revenat.ishop.ui.filter.RequestUrlMemorizerFilter;
import com.revenat.ishop.ui.filter.ShoppingCartDeserializationFilter;
import com.revenat.ishop.ui.listener.IShopApplicationListener;
import com.revenat.ishop.ui.service.ShoppingCartCookieSerializer;

public class IShopWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
		WebApplicationContext appContext = createWebApplicaitonContext(container);
		
		container.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		container.addListener(new ContextLoaderListener(appContext));
		container.addListener(new IShopApplicationListener(appContext.getBean(CategoryService.class)));
		
		container.setAttribute(Attribute.I18N_SERVICE, appContext.getBean(I18nService.class));
		
		registerServlets(container, appContext);
		
		registerFilters(container, appContext);
		
	}

	@SuppressWarnings("unchecked")
	private void registerFilters(ServletContext container, WebApplicationContext appContext) {
		FilterRegistration.Dynamic filterReg = container.addFilter("ErrorHandlerFilter",
				new ErrorHandlerFilter(appContext.getBean(I18nService.class)));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				false,
				"/*");
		
		filterReg = container.addFilter("HtmlMinificationFilter", HtmlMinificationFilter.class);
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = container.addFilter("CategoriesAndProducersLoaderFilter",
				new CategoriesAndProducersLoaderFilter(
						appContext.getBean(CategoryService.class),
						appContext.getBean(ProducerService.class)));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = container.addFilter("ShoppingCartDeserializationFilter",new ShoppingCartDeserializationFilter(
				new ShoppingCartCookieSerializer(appContext.getBean(ShoppingCartMapper.class))));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = container.addFilter("SetCurrentRequestUriFilter",new RequestUrlMemorizerFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = container.addFilter("ClientLocaleDefinerFilter",new ClientLocaleDefinerFilter());
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				"/*");
		
		filterReg = container.addFilter("AuthenticationFilter",
				new AuthenticationFilter(appContext.getBean(AuthenticationService.class)));
		filterReg.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST),
				true,
				URL.MY_ORDERS, URL.ORDER, URL.AJAX_MORE_ORDERS);
	}

	private void registerServlets(ServletContext container, WebApplicationContext appContext) {
		ServletRegistration.Dynamic servletReg = container.addServlet("AllProductsController",
				new AllProductsController(appContext.getBean(ProductService.class)));
		servletReg.addMapping(URL.ALL_PRODUCTS);
		
		servletReg = container.addServlet("ErrorController", ErrorController.class);
		servletReg.addMapping(URL.ERROR);
		
		servletReg = container.addServlet("ProductsByCategoryController",
				new ProductsByCategoryController(appContext.getBean(ProductService.class)));
		servletReg.addMapping(URL.PRODUCTS_BY_CATEGORY);
		
		servletReg = container.addServlet("SearchController",
				new ProductSearchController(appContext.getBean(ProductService.class)));
		servletReg.addMapping(URL.SEARCH);
		
		servletReg = container.addServlet("ShoppingCartController", new ShoppingCartController());
		servletReg.addMapping(URL.SHOPPING_CART);
		
		servletReg = container.addServlet("SignInController",
				new SignInController(appContext.getBean(AuthenticationService.class)));
		servletReg.addMapping(URL.SIGN_IN);
		
		servletReg = container.addServlet("SignOutController",
				new SignOutController(appContext.getBean(AuthenticationService.class)));
		servletReg.addMapping(URL.SIGN_OUT);
		
		servletReg = container.addServlet("SocialLoginController",
				new SocialLoginController(appContext.getBean(AuthenticationService.class)));
		servletReg.addMapping(URL.SOCIAL_LOGIN);
		
		servletReg = container.addServlet("MyOrdersController",
				new MyOrdersController(appContext.getBean(OrderManager.class)));
		servletReg.addMapping(URL.MY_ORDERS);
		
		servletReg = container.addServlet("OrderController",
				new OrderController(appContext.getBean(OrderManager.class), appContext.getBean(I18nService.class)));
		servletReg.addMapping(URL.ORDER);
		
		servletReg = container.addServlet("LoadMoreAllProductsController",
				new LoadMoreAllProductsController(appContext.getBean(ProductService.class)));
		servletReg.addMapping(URL.AJAX_MORE_PRODUCTS);
		
		servletReg = container.addServlet("LoadMoreProductsByCategoryController",
				new LoadMoreProductsByCategoryController(appContext.getBean(ProductService.class)));
		servletReg.addMapping(URL.AJAX_MORE_PRODUCTS_BY_CATEGORY);
		
		servletReg = container.addServlet("LoadMoreProductsForSearchResultController",
				new LoadMoreProductsForSearchResultController(appContext.getBean(ProductService.class)));
		servletReg.addMapping(URL.AJAX_MORE_SEARCH);
		
		servletReg = container.addServlet("AddProductToShoppingCartController",
				new AddProductToShoppingCartController(appContext.getBean(ShoppingCartService.class)));
		servletReg.addMapping(URL.AJAX_ADD_PRODUCT_TO_CART);
		
		servletReg = container.addServlet("RemoveProductFromShoppingCartController",
				new RemoveProductFromShoppingCartController(appContext.getBean(ShoppingCartService.class)));
		servletReg.addMapping(URL.AJAX_REMOVE_PRODUCT_FROM_CART);
		
		servletReg = container.addServlet("LoadMoreOrdersController",
				new LoadMoreOrdersController(appContext.getBean(OrderManager.class)));
		servletReg.addMapping(URL.AJAX_MORE_ORDERS);
	}

	private WebApplicationContext createWebApplicaitonContext(ServletContext container) {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.scan("com.revenat.ishop.infrastructure.config");
		ctx.setServletContext(container);
		ctx.refresh();
		return ctx;
	}

}
