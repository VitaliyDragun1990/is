package com.revenat.ishop.model;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.revenat.ishop.entity.Product;
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
		Product p = createProductWithId(1);
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
		Product p = createProductWithId(1);
		shoppingCart.addProduct(p, 1);
		shoppingCart.addProduct(p, 2);

		assertContainsItemWithQuantity(shoppingCart, 1, 3);
	}

	@Test
	public void shouldAllowToDecrementProductUnitQuantityIfRemoveProductSeveralTimes() throws Exception {
		Product p = createProductWithId(1);
		shoppingCart.addProduct(p, 8);

		shoppingCart.removeProduct(p.getId(), 2);
		shoppingCart.removeProduct(p.getId(), 3);

		assertContainsItemWithQuantity(shoppingCart, 1, 3);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductWithNegativeQuantity() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), -1);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductWithZeroQuantity() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), 0);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductWithQuantityGreaterThan10() throws Exception {
		shoppingCart.addProduct(createProductWithId(1), 11);
	}

	@Test(expected = ValidationException.class)
	public void shouldNotAllowToAddProductIfItTotalQuantityWouldBeGreaterThan10() throws Exception {
		Product p = createProductWithId(1);
		shoppingCart.addProduct(p, 5);
		shoppingCart.addProduct(p, 6);
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
			cart.addProduct(createProductWithId(i), 1);
		}

	}
	
	private static Product createProductWithId(int id) {
		Product p = new Product();
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
