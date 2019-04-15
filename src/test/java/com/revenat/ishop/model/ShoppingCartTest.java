package com.revenat.ishop.model;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ShoppingCart.ShoppingCartItem;

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
		shoppingCart.addProduct(1, 1);

		assertThat(shoppingCart.getTotalCount(), is(1));
	}

	@Test
	public void shouldAllowToAddSeveralProductsToShoppingCart() throws Exception {
		shoppingCart.addProduct(1, 1);
		shoppingCart.addProduct(2, 1);
		shoppingCart.addProduct(3, 1);

		assertThat(shoppingCart.getTotalCount(), is(3));
	}

	@Test
	public void shouldAllowToRemoveSpecifiedNumberOfProductUnitsFromShoppingCart() throws Exception {
		shoppingCart.addProduct(1, 5);

		shoppingCart.removeProduct(1, 2);

		assertThat(shoppingCart.getTotalCount(), is(3));
	}

	@Test
	public void shouldRemoveProductFromCartIfNumberToRemoveGreaterThanStoredNumber() throws Exception {
		shoppingCart.addProduct(1, 5);
		shoppingCart.addProduct(2, 2);

		shoppingCart.removeProduct(2, 5);

		assertThat(shoppingCart.getTotalCount(), is(5));
	}

	@Test
	public void shouldAllowToGetAllShoppingCartItems() throws Exception {
		shoppingCart.addProduct(1, 5);
		shoppingCart.addProduct(2, 2);

		Collection<ShoppingCartItem> items = shoppingCart.getItems();

		assertThat(items, hasSize(2));
	}

	@Test
	public void shouldAllowToIncrementProductUnitQuantityIfAddProductSeveralTimes() throws Exception {
		shoppingCart.addProduct(1, 1);
		shoppingCart.addProduct(1, 2);

		assertContainsItemWithQuantity(shoppingCart, 1, 3);
	}

	@Test
	public void shouldAllowToDecrementProductUnitQuantityIfRemoveProductSeveralTimes() throws Exception {
		shoppingCart.addProduct(1, 8);

		shoppingCart.removeProduct(1, 2);
		shoppingCart.removeProduct(1, 3);

		assertContainsItemWithQuantity(shoppingCart, 1, 3);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductWithNegativeQuantity() throws Exception {
		shoppingCart.addProduct(1, -1);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductWithZeroQuantity() throws Exception {
		shoppingCart.addProduct(1, 0);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductWithQuantityGreaterThan10() throws Exception {
		shoppingCart.addProduct(1, 11);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductIfItTotalQuantityWouldBeGreaterThan10() throws Exception {
		shoppingCart.addProduct(1, 5);
		shoppingCart.addProduct(1, 6);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToRemoveNegativeNumberOfProductUnits() throws Exception {
		shoppingCart.removeProduct(1, -1);
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

	private static void populateShoppingCart(ShoppingCart cart, int numberOfProducts) {
		for (int i = 1; i <= numberOfProducts; i++) {
			cart.addProduct(i, 1);
		}

	}

	private static void assertContainsItemWithQuantity(ShoppingCart cart, int productId, int quantity) {
		Collection<ShoppingCartItem> items = cart.getItems();

		assertThat(items, hasItems(allOf(hasProperty("productId", equalTo(1)), hasProperty("quantity", equalTo(3)))));
	}

}
