package com.revenat.ishop.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.model.ShoppingCart;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartUtilsTest {
	
	@Mock
	private HttpSession httpSession;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpServletRequest request;
	@Captor
	private ArgumentCaptor<Cookie> captor;

	@Test
	public void shouldAllowToGetCurrentShoppingCartFromHttpSession() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		when(httpSession.getAttribute(anyString())).thenReturn(cart);
		
		ShoppingCart currentShoppingCart = ShoppingCartUtils.getCurrentShoppingCart(httpSession);
		
		assertThat(currentShoppingCart, sameInstance(cart));
	}
	
	@Test
	public void shouldPlaceNewShoppingCartIntoSessionIfThereIsNoOneInSession() throws Exception {
		when(httpSession.getAttribute(anyString())).thenReturn(null);
		
		ShoppingCart currentShoppingCart = ShoppingCartUtils.getCurrentShoppingCart(httpSession);
		
		assertThat(currentShoppingCart, notNullValue());
		verify(httpSession).setAttribute(eq(Constants.CURRENT_SHOPPING_CART), isA(ShoppingCart.class));
	}

	@Test
	public void shouldAllowToSetShoppingCartIntoSession() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		
		ShoppingCartUtils.setCurrentShoppingCart(httpSession, cart);
		
		verify(httpSession).setAttribute(eq(Constants.CURRENT_SHOPPING_CART), same(cart));
	}
	
	@Test
	public void shouldReturnTrueIfSessionContainsShoppingCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		when(httpSession.getAttribute(anyString())).thenReturn(cart);
		
		boolean isExist = ShoppingCartUtils.isCurrentShoppingCartExists(httpSession);
		
		assertTrue("Should be true if cart exists in session", isExist);
	}
	
	@Test
	public void shouldReturnFalseIfNoSessionCartInSession() throws Exception {
		when(httpSession.getAttribute(anyString())).thenReturn(null);
		
		boolean isExist = ShoppingCartUtils.isCurrentShoppingCartExists(httpSession);
		
		assertFalse("Should be false if no cart exists in session", isExist);
	}
	
	@Test
	public void shouldAllowToRemoveShoppingCartFromSession() throws Exception {
		ShoppingCartUtils.removeCurrentShoppingCart(httpSession, response);
		
		verify(httpSession).removeAttribute(eq(Constants.CURRENT_SHOPPING_CART));
	}
	
	@Test
	public void shouldSetShoppingCartCookieForRemovalWhenRemoveShoppingCart() throws Exception {
		ShoppingCartUtils.removeCurrentShoppingCart(httpSession, response);
		
		verify(response).addCookie(captor.capture());
		Cookie cartCookie = captor.getValue();
		assertThat(cartCookie.getName(), equalTo(Constants.Cookie.SHOPPING_CART.getName()));
		assertThat(cartCookie.getMaxAge(), equalTo(0));
	}
	
	@Test
	public void shouldAllowToGetShoppingCartCookieFromRequest() throws Exception {
		Cookie cartCookie = new Cookie(Constants.Cookie.SHOPPING_CART.getName(), "some_value");
		when(request.getCookies()).thenReturn(new Cookie[] {cartCookie});
		
		Cookie result = ShoppingCartUtils.findShoppingCartCookie(request);
		
		assertThat(result, sameInstance(cartCookie));
	}
	
	@Test
	public void shouldReturnNullIfNoShoppingCartCookieInTheRequest() throws Exception {
		when(request.getCookies()).thenReturn(new Cookie[0]);
		
		Cookie result = ShoppingCartUtils.findShoppingCartCookie(request);
		
		assertThat(result, nullValue());
	}
	
	@Test
	public void shouldAllowToSetShoppingCartCookie() throws Exception {
		String cookieValue = "shoppingCartCookieValue";
		
		ShoppingCartUtils.setCurrentShoppingCartCookie(cookieValue, response);
		
		verify(response).addCookie(captor.capture());
		Cookie shoppingCartCookie = captor.getValue();
		assertThat(shoppingCartCookie.getValue(), equalTo(cookieValue));
	}
}
