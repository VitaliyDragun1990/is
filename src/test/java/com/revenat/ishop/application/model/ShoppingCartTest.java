package com.revenat.ishop.application.model;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.model.ShoppingCart.ShoppingCartItem;
import com.revenat.ishop.infrastructure.exception.flow.ValidationException;

public class ShoppingCartTest {

	private ShoppingCart shoppingCart;

	@Before
	public void setUp() {
		shoppingCart = new ShoppingCart();
	}

	@Test
	public void newShoppingCartShouldBeEmpty() throws Exception {
		assertThat(shoppingCart.getTotalCount(), is(0));
	}

	@Test
	public void shouldAllowToAddProductToShoppingCart() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), 1);

		assertThat(shoppingCart.getTotalCount(), is(1));
	}

	@Test
	public void shouldAllowToAddSeveralProductsToShoppingCart() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), 1);
		shoppingCart.addProduct(createProductWithId(2), 1);
		shoppingCart.addProduct(createProductWithId(3), 1);

		assertThat(shoppingCart.getTotalCount(), is(3));
	}

	@Test
	public void shouldAllowToRemoveSpecifiedNumberOfProductUnitsFromShoppingCart() throws Exception {
		ProductDTO p = createProductWithId(1);
		shoppingCart.addProduct(p, 5);

		shoppingCart.removeProduct(p.getId(), 2);

		assertThat(shoppingCart.getTotalCount(), is(3));
	}

	@Test
	public void shouldRemoveProductFromCartIfNumberToRemoveGreaterThanStoredNumber() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), 5);
		shoppingCart.addProduct(createProductWithId(2), 2);

		shoppingCart.removeProduct(2, 5);

		assertThat(shoppingCart.getTotalCount(), is(5));
	}

	@Test
	public void shouldAllowToGetAllShoppingCartItems() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), 5);
		shoppingCart.addProduct(createProductWithId(2), 2);

		Collection<ShoppingCartItem> items = shoppingCart.getItems();

		assertThat(items, hasSize(2));
	}

	@Test
	public void shouldAllowToIncrementProductUnitQuantityIfAddProductSeveralTimes() throws Exception {
		ProductDTO p = createProductWithId(1);
		shoppingCart.addProduct(p, 1);
		shoppingCart.addProduct(p, 2);

		assertContainsItemWithQuantity(shoppingCart, 1, 3);
	}

	@Test
	public void shouldAllowToDecrementProductUnitQuantityIfRemoveProductSeveralTimes() throws Exception {
		ProductDTO p = createProductWithId(1);
		shoppingCart.addProduct(p, 8);

		shoppingCart.removeProduct(p.getId(), 2);
		shoppingCart.removeProduct(p.getId(), 3);

		assertContainsItemWithQuantity(shoppingCart, 1, 3);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductIfItTotalQuantityWouldBeGreaterThan10() throws Exception {
		ProductDTO p = createProductWithId(1);
		shoppingCart.addProduct(p, 5);
		shoppingCart.addProduct(p, 6);
	}

	@Test
	public void shouldAllowToStoreUpTo20DifferentProducts() throws Exception {
		populateShoppingCart(shoppingCart, 20);

		Collection<ShoppingCartItem> items = shoppingCart.getItems();
		assertThat(items, hasSize(20));
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToStoreMoreThan20DifferentProducts() throws Exception {
		populateShoppingCart(shoppingCart, 21);
	}
	
	@Test
	public void shouldBeEmptyIfDoesNotContainAnyProduct() throws Exception {
		assertTrue(shoppingCart.isEmpty());
	}
	
	@Test
	public void shouldNotBeEmptyIfContainsAnyProduct() throws Exception {
		populateShoppingCart(shoppingCart, 2);
		
		assertFalse(shoppingCart.isEmpty());
	}
	
	@Test
	public void emptyShoppingCartShouldHaveTotalCostOfZero() throws Exception {
		assertTrue(shoppingCart.isEmpty());
		assertThat(shoppingCart.getTotalCost(), equalTo(BigDecimal.ZERO));
	}
	
	@Test
	public void shouldAllowToGetCartTotalCost() throws Exception {
		ProductDTO productA = createProductWithId(1);
		productA.setPrice(BigDecimal.valueOf(10.45));
		ProductDTO productB = createProductWithId(2);
		productB.setPrice(BigDecimal.valueOf(20.50));
		
		shoppingCart.addProduct(productA, 1);
		shoppingCart.addProduct(productB, 10);
		
		BigDecimal expectedTotalCost = 
				BigDecimal.valueOf(10.45)
				.add(
						BigDecimal.valueOf(20.50).multiply(BigDecimal.valueOf(10))
						);
		assertThat(shoppingCart.getTotalCost(), equalTo(expectedTotalCost));
	}
	
	@Test
	public void shouldAllowToClearShoppingCart() throws Exception {
		ProductDTO productA = createProductWithId(1);
		productA.setPrice(BigDecimal.valueOf(10.45));
		ProductDTO productB = createProductWithId(2);
		productB.setPrice(BigDecimal.valueOf(20.50));
		shoppingCart.addProduct(productA, 1);
		shoppingCart.addProduct(productB, 10);
		assertFalse(shoppingCart.isEmpty());
		
		shoppingCart.clear();
		
		assertTrue(shoppingCart.isEmpty());
	}

	private static void populateShoppingCart(ShoppingCart cart, int numberOfProducts) {
		for (int i = 1; i <= numberOfProducts; i++) {
			cart.addProduct(createProductWithId(i), 1);
		}

	}
	
	private static ProductDTO createProductWithId(int id) {
		ProductDTO p = new ProductDTO();
		p.setId(id);
		return p;
	}

	private static void assertContainsItemWithQuantity(ShoppingCart cart, int productId, int quantity) {
		Collection<ShoppingCartItem> items = cart.getItems();

		assertThat(items, hasItems(allOf(
				hasProperty("product", hasProperty("id", equalTo(productId))),
				hasProperty("quantity", equalTo(3))
				)));
	}

}
