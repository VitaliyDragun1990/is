package com.revenat.ishop.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;

public class ShoppingCartCookieMapperTest {

	private ShoppingCartCookieMapper mapper = new ShoppingCartCookieMapper();
	
	@Test
	public void shouldAllowToMarshallShoppingCartWithProductsIntoCookieString() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		cart.addProduct(1, 5);
		cart.addProduct(2, 3);
		
		String cookieString = mapper.toString(cart);
		
		assertThat(cookieString, containsString("1-5"));
		assertThat(cookieString, containsString("2-3"));
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToMarshallNullShoppingCart() throws Exception {
		mapper.toString(null);
	}
	
	@Test
	public void shouldAllowToUnmarshallShoppingCartFromCookieString() throws Exception {
		String cookieString = "1-5|25-10|18-5";
		
		ShoppingCart shoppingCart = mapper.fromString(cookieString);
		
		assertThat(shoppingCart.getTotalCount(), equalTo(20));
		assertContainsItemWithQuantity(shoppingCart, 1, 5);
		assertContainsItemWithQuantity(shoppingCart, 25, 10);
		assertContainsItemWithQuantity(shoppingCart, 18, 5);
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotAllowToUnmarshallNullCookieString() throws Exception {
		mapper.fromString(null);
	}
	
	@Test
	public void shouldReturnEmptyShoppingCartIfCanNotUnmarshallCookieString() throws Exception {
		String invalidCookieString = "adaw|+|";
		
		ShoppingCart cart = mapper.fromString(invalidCookieString);
		
		assertThat(cart.getTotalCount(), equalTo(0));
	}
	
	@Test
	public void shouldDropInvalidCartItemSegmentIfExist() throws Exception {
		String cookieStringWithoutInvalidSegment = "1-5|25-";
		
		ShoppingCart shoppingCart = mapper.fromString(cookieStringWithoutInvalidSegment);
		
		assertThat(shoppingCart.getTotalCount(), equalTo(5));
		assertContainsItemWithQuantity(shoppingCart, 1, 5);
	}
	
	private static void assertContainsItemWithQuantity(ShoppingCart cart, int productId, int quantity) {
		Collection<ShoppingCartItem> items = cart.getItems();

		assertThat(items, hasItems(allOf(hasProperty("productId", equalTo(productId)), hasProperty("quantity", equalTo(quantity)))));
	}
}
