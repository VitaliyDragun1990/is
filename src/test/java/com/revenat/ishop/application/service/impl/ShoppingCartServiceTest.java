package com.revenat.ishop.application.service.impl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.model.ShoppingCart;
import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;
import com.revenat.ishop.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.ishop.infrastructure.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ShoppingCartServiceTest {
	private static final int PRODUCT_ID = 1;
	private static final BigDecimal PRODUCT_PRICE = BigDecimal.TEN;
	
	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Mock
	private ProductRepository productRepository;

	private ShoppingCartService service;

	@Before
	public void setUp() {
		service = new ShoppingCartService(productRepository);
		
		Product p = createProductWithId(PRODUCT_ID);
		when(productRepository.getById(PRODUCT_ID)).thenReturn(p);
	}

	@Test
	public void shouldAllowToAddProductToShoppingCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		assertTrue(cart.isEmpty());
		
		int quantity = 5;
		service.addProductToShoppingCart(PRODUCT_ID, quantity, cart);
		
		assertFalse(cart.isEmpty());
		assertThat(cart.getTotalCount(), equalTo(quantity));
		assertThat(cart.getTotalCost(), equalTo(PRODUCT_PRICE.multiply(BigDecimal.valueOf(quantity))));
	}
	
	@Test
	public void shouldNowAllowToAddProductToCartUsingWrongProductId() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		int wrongProductId = 10;
		int quantity = 5;
		
		expected.expect(ResourceNotFoundException.class);
		expected.expectMessage(containsString("product with id: " + wrongProductId + " not found."));
		service.addProductToShoppingCart(wrongProductId, quantity, cart);
	}
	
	@Test
	public void shouldNotAllowToAddProductWithNegativeQuantity() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		int quantity = -1;
		
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString(String.format("valid product quantity should be between 1 and %d inclusive.",
				ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART)));
		
		service.addProductToShoppingCart(PRODUCT_ID, quantity, cart);
	}
	
	@Test
	public void shouldNotAllowToAddProductWithQuantityLargeThatAllowed() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		int quantity = ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART + 1;
		
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString(String.format("valid product quantity should be between 1 and %d inclusive.",
				ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART)));
		
		service.addProductToShoppingCart(PRODUCT_ID, quantity, cart);
	}
	
	@Test
	public void shouldNotAllowToRemoveProductWithNegativeQuantity() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		int quantity = -1;
		
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString(String.format("valid product quantity should be between 1 and %d inclusive.",
				ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART)));
		
		service.removeProductFromShoppingCart(PRODUCT_ID, quantity, cart);
	}
	
	@Test
	public void shouldNotAllowToRemoveProductWithQuantityLargeThatAllowed() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		int quantity = ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART + 1;
		
		expected.expect(InvalidParameterException.class);
		expected.expectMessage(containsString(String.format("valid product quantity should be between 1 and %d inclusive.",
				ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART)));
		
		service.removeProductFromShoppingCart(PRODUCT_ID, quantity, cart);
	}
	
	@Test
	public void shouldAllowToRemoveProductFromShoppingCart() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		Product p = createProductWithId(PRODUCT_ID);
		int initialQuantity = 5;
		cart.addProduct(p, initialQuantity);
		assertThat(cart.getTotalCount(), equalTo(initialQuantity));
		
		int quantityToRemove = 3;
		service.removeProductFromShoppingCart(PRODUCT_ID, quantityToRemove, cart);
		
		assertThat(cart.getTotalCount(), equalTo(initialQuantity - quantityToRemove));
	}
	
	@Test
	public void shouldRemoveProductCompletelyIfQuantityToRemoveGreaterThatActualQuantity() throws Exception {
		ShoppingCart cart = new ShoppingCart();
		Product p = createProductWithId(PRODUCT_ID);
		int initialQuantity = 5;
		cart.addProduct(p, initialQuantity);
		assertThat(cart.getTotalCount(), equalTo(initialQuantity));
		
		int quantityToRemove = 7;
		service.removeProductFromShoppingCart(PRODUCT_ID, quantityToRemove, cart);
		
		assertTrue(cart.isEmpty());
	}
	
	private Product createProductWithId(int productId) {
		Product p = new Product();
		p.setId(productId);
		p.setPrice(PRODUCT_PRICE);
		return p;
	}
}
