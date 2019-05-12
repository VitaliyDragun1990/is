package com.revenat.ishop.application.service;

import com.revenat.ishop.application.domain.entity.Product;
import com.revenat.ishop.application.domain.model.ShoppingCart;
import com.revenat.ishop.application.infra.exception.ResourceNotFoundException;
import com.revenat.ishop.application.infra.exception.flow.InvalidParameterException;
import com.revenat.ishop.application.infra.exception.flow.ValidationException;
import com.revenat.ishop.persistence.repository.ProductRepository;

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
		validateProductQuantity(quantity);
		Product product = productRepository.getById(productId);
		if (product == null) {
			throw new ResourceNotFoundException(
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
		validateProductQuantity(quantity);
		shoppingCart.removeProduct(productId, quantity);
	}
	
	private static void validateProductQuantity(int quantity) {
		if (quantity < 1 || quantity > ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART) {
			throw new InvalidParameterException(String.format("valid product quantity should be between 1 and %d inclusive.",
					ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART));
		}
	}
}
