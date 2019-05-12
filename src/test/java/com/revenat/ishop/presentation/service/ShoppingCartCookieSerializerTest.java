package com.revenat.ishop.presentation.service;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.application.domain.model.ShoppingCart;
import com.revenat.ishop.application.mapper.ShoppingCartMapper;
import com.revenat.ishop.presentation.infra.config.Constants;
import com.revenat.ishop.presentation.service.ShoppingCartCookieSerializer;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartCookieSerializerTest {

	@Mock
	private ShoppingCartMapper<String> cartMapper;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpServletRequest request;

	@Captor
	private ArgumentCaptor<Cookie> cookieCaptor;
	
	private ShoppingCartCookieSerializer cartSerializer;
	
	@Before
	public void setUp() {
		cartSerializer = new ShoppingCartCookieSerializer(cartMapper);
	}
	
	@Test
	public void shouldAllowToRemoveShoppingCartCookie() throws Exception {
		cartSerializer.removeShoppingCartCookie(response);
		
		verify(response).addCookie(cookieCaptor.capture());
		Cookie cartCookie = cookieCaptor.getValue();
		assertThat(cartCookie.getName(), equalTo(Constants.Cookie.SHOPPING_CART.getName()));
		assertThat(cartCookie.getMaxAge(), equalTo(0));
	}
	
	@Test
	public void shouldAllowToGetShoppingCartFromCookie() throws Exception {
		Cookie shoppingCartCookie = new Cookie(Constants.Cookie.SHOPPING_CART.getName(), "test-value");
		when(request.getCookies()).thenReturn(new Cookie[] {shoppingCartCookie});
		ShoppingCart fromCookie = new ShoppingCart();
		when(cartMapper.unmarshall(anyString())).thenReturn(fromCookie);
		
		ShoppingCart shoppingCart = cartSerializer.getShoppingCartFromCookie(request);
		
		assertThat(shoppingCart, sameInstance(fromCookie));
	}
	
	@Test
	public void shouldReturnNullIfCanNotGetShoppingCartFromCookie() throws Exception {
		ShoppingCart shoppingCart = cartSerializer.getShoppingCartFromCookie(request);
		
		assertNull(shoppingCart);
	}
	
	@Test
	public void shouldAllowToSaveShoppingCartAsCookie() throws Exception {
		ShoppingCart shoppingCart = new ShoppingCart();
		
		cartSerializer.saveShoppingCartAsCookie(shoppingCart, response);
		
		verify(response).addCookie(cookieCaptor.capture());
		Cookie cartCookie = cookieCaptor.getValue();
		assertThat(cartCookie.getName(), equalTo(Constants.Cookie.SHOPPING_CART.getName()));
		assertThat(cartCookie.getMaxAge(), equalTo(Constants.Cookie.SHOPPING_CART.getTtl()));
	}
	
	@Test
	public void shouldAllowToFindShoppingCartCookie() throws Exception {
		Cookie shoppingCartCookie = new Cookie(Constants.Cookie.SHOPPING_CART.getName(), "test-value");
		Cookie anotherCookie = new Cookie("some-name", "test-value");
		when(request.getCookies()).thenReturn(new Cookie[] {shoppingCartCookie, anotherCookie});
		
		Cookie foundCookie = cartSerializer.findShoppingCartCookie(request);
		assertThat(foundCookie, equalTo(shoppingCartCookie));
	}
}
