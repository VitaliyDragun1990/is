package com.revenat.ishop.repository;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.entity.Product;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.service.application.ShoppingCartMapper;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartRepositoryTest {

	@Mock
	private ShoppingCartMapper<String> cartMapper;
	@Mock
	private HttpSession httpSession;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpServletRequest request;
	@Captor
	private ArgumentCaptor<ShoppingCart> cartCaptor;
	
	@Captor
	private ArgumentCaptor<Cookie> cookieCaptor;

	private ShoppingCart cart;

	private ShoppingCartRepository repository;

	@Before
	public void setUp() {
		repository = new ShoppingCartRepository(cartMapper);

		Product p = new Product();
		p.setPrice(BigDecimal.TEN);
		cart = new ShoppingCart();
		cart.addProduct(p, 1);
		
		when(request.getSession()).thenReturn(httpSession);
	}

	@Test
	public void shouldAllowToGetShoppingCartFromClientSession() throws Exception {
		when(httpSession.getAttribute(anyString())).thenReturn(cart);

		ShoppingCart clientsCart = repository.getShoppingCart(httpSession);

		assertNotNull(clientsCart);
	}

	@Test
	public void shouldReturnNullIfNoShoppingCartIsSession() throws Exception {
		ShoppingCart clientsCart = repository.getShoppingCart(httpSession);

		assertNull(clientsCart);
	}
	
	@Test
	public void shouldAllowToSetShoppingCartIntoClientSessin() throws Exception {
		repository.setShoppingCart(httpSession, cart);
		
		verify(httpSession).setAttribute(anyString(), same(cart));
	}
	
	@Test
	public void loadingDoesNothingIfSessionContainsShoppingCart() throws Exception {
		when(httpSession.getAttribute(anyString())).thenReturn(cart);
		
		repository.loadShoppingCart(request);
		
		verify(httpSession, times(0)).setAttribute(anyString(), Mockito.any(ShoppingCart.class));
	}
	
	@Test
	public void shouldTryToLoadCartFromCookieIfNoCartInSession() throws Exception {
		Cookie shoppingCartCookie = new Cookie("iSCC", "test-value");
		when(request.getCookies()).thenReturn(new Cookie[] {shoppingCartCookie});
		ShoppingCart fromCookie = new ShoppingCart();
		when(cartMapper.unmarshall(anyString())).thenReturn(fromCookie);
		
		repository.loadShoppingCart(request);
		
		verify(httpSession).setAttribute(anyString(), Mockito.same(fromCookie));
	}
	
	@Test
	public void shouldPlaceNewCartInSessionIfNotCartIsSessionAndNoCartInCookie() throws Exception {
		repository.loadShoppingCart(request);
		
		verify(httpSession).setAttribute(anyString(), cartCaptor.capture());
		ShoppingCart newCart = cartCaptor.getValue();
		assertTrue(newCart.isEmpty());
	}
	
	@Test
	public void shouldAllowToPersistCartAsCookie() throws Exception {
		repository.persistShoppingCartAsCookie(cart, response);
		
		verify(response).addCookie(Mockito.any());
	}
	
	@Test
	public void shouldAllowToRemoveShoppingCartCookie() throws Exception {
		repository.removeShoppingCartCookie(response);
		
		verify(response).addCookie(cookieCaptor.capture());
		Cookie cartCookie = cookieCaptor.getValue();
		assertThat(cartCookie.getMaxAge(), equalTo(0));
	}
}
