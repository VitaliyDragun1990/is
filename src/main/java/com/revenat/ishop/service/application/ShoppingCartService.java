package com.revenat.ishop.service.application;

import com.revenat.ishop.entity.Product;
import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.repository.ProductRepository;

/**
 * This component incapsulates CRUD shopping cart logic. It contains various
 * methods for retrieving, updating, setting, deleting client's
 * {@link ShoppingCart} object.
 * 
 * @author Vitaly Dragun
 *
 */
public class ShoppingCartService {
	private final ProductRepository productRepository;

	public ShoppingCartService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	/**
	 * Adds {@link Product} with provided {@code productId} in the amount of
	 * {@code quantity} to the provided {@code ShoppingCart} instance.
	 * 
	 * @param productId    unique identifier of the {@link Product} entity to add to
	 *                     shopping cart
	 * @param quantity     amount of product units to add
	 * @param shoppingCart {@link ShoppingCart} object to which product will be
	 *                     added
	 * @throws ValidationException if there is no product with provided
	 *                             {@code productId}
	 */
	public void addProductToShoppingCart(int productId, int quantity, ShoppingCart shoppingCart) {
		Product product = productRepository.getById(productId);
		if (product == null) {
			throw new ValidationException(
					"Error during adding product to shopping cart: " + "product with id: " + productId + " not found.");
		}
		shoppingCart.addProduct(product, quantity);
	}

	/**
	 * Removes {@link Product} with provided {@code productId} in the amount of
	 * {@code quantity} from the provided {@code ShoppingCart} instance.
	 * 
	 * @param productId    unique identifier of the {@link Product} entity
	 * @param quantity     amount of product units to remove
	 * @param shoppingCart {@link ShoppingCart} object from which product will be
	 *                     removed
	 */
	public void removeProductFromShoppingCart(int productId, int quantity, ShoppingCart shoppingCart) {
		shoppingCart.removeProduct(productId, quantity);
	}
}
