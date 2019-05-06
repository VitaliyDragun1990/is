package com.revenat.ishop.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.revenat.ishop.entity.Product;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.repository.ProductRepository;
import com.revenat.ishop.service.application.impl.ShoppingCartCookieStringMapper;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartCookieMapperTest {
	
	@Mock
	private ProductRepository productRepository;

	private ShoppingCartCookieStringMapper mapper;
	
	@Before
	public void setup() {
		mapper = new ShoppingCartCookieStringMapper(productRepository);
		when(productRepository.getById(Mockito.any(Integer.class))).thenAnswer(new Answer<Product>() {
			@Override
			public Product answer(InvocationOnMock invocation) throws Throwable {
				int productId = invocation.getArgument(0);
				Product p = new Product();
				p.setId(productId);
				return p;
			}
		});
	}
	
	@Test
	public void shouldAllowToMarshallShoppingCartWithProductsIntoCookieString() throws Exception {
		Product productA = new Product();
		productA.setId(1);
		Product productB = new Product();
		productB.setId(2);
		
		ShoppingCart cart = new ShoppingCart();
		cart.addProduct(productA, 5);
		cart.addProduct(productB, 3);
		
		String cookieString = mapper.marshall(cart);
		
		assertThat(cookieString, containsString("1-5"));
		assertThat(cookieString, containsString("2-3"));
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToMarshallNullShoppingCart() throws Exception {
		mapper.marshall(null);
	}
	
	@Test
	public void shouldAllowToUnmarshallShoppingCartFromCookieString() throws Exception {
		String cookieString = "1-5|25-10|18-5";
		
		ShoppingCart shoppingCart = mapper.unmarshall(cookieString);
		
		assertThat(shoppingCart.getTotalCount(), equalTo(20));
		assertContainsItemWithQuantity(shoppingCart, 1, 5);
		assertContainsItemWithQuantity(shoppingCart, 25, 10);
		assertContainsItemWithQuantity(shoppingCart, 18, 5);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToUnmarshallNullCookieString() throws Exception {
		mapper.unmarshall(null);
	}
	
	@Test
	public void shouldReturnEmptyShoppingCartIfCanNotUnmarshallCookieString() throws Exception {
		String invalidCookieString = "adaw|+|";
		
		ShoppingCart cart = mapper.unmarshall(invalidCookieString);
		
		assertThat(cart.getTotalCount(), equalTo(0));
	}
	
	@Test
	public void shouldDropInvalidCartItemSegmentIfExist() throws Exception {
		String cookieStringWithoutInvalidSegment = "1-5|25-";
		
		ShoppingCart shoppingCart = mapper.unmarshall(cookieStringWithoutInvalidSegment);
		
		assertThat(shoppingCart.getTotalCount(), equalTo(5));
		assertContainsItemWithQuantity(shoppingCart, 1, 5);
	}
	
	private static void assertContainsItemWithQuantity(ShoppingCart cart, int productId, int quantity) {
		Collection<ShoppingCartItem> items = cart.getItems();

		assertThat(items, hasItems(allOf(
				hasProperty("product", hasProperty("id", equalTo(productId))),
				hasProperty("quantity", equalTo(quantity))
				)));
	}
}
