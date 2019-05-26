package com.revenat.ishop.application.service;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.model.ShoppingCart;
import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;

/**
 * This component incapsulates CRUD shopping cart logic. It contains various
 * methods for retrieving, updating, setting, deleting client's
 * {@link ShoppingCart} object.
 * 
 * @author Vitaly Dragun
 *
 */
public interface ShoppingCartService {

	/**
	 * Adds {@link Product} with provided {@code productId} in the amount of
	 * {@code quantity} to the provided {@code ShoppingCart} instance.
	 * 
	 * @param productId    unique identifier of the {@link Product} entity to add to
	 *                     shopping cart
	 * @param quantity     amount of product units to add
	 * @param shoppingCart {@link ShoppingCart} object to which product will be
	 *                     added
	 * @throws ResourceNotFoundException if there is no product with provided
	 *                             {@code productId}
	 */
	void addProductToShoppingCart(int productId, int quantity, ShoppingCart shoppingCart);

	/**
	 * Removes {@link Product} with provided {@code productId} in the amount of
	 * {@code quantity} from the provided {@code ShoppingCart} instance.
	 * 
	 * @param productId    unique identifier of the {@link Product} entity
	 * @param quantity     amount of product units to remove
	 * @param shoppingCart {@link ShoppingCart} object from which product will be
	 *                     removed
	 */
	void removeProductFromShoppingCart(int productId, int quantity, ShoppingCart shoppingCart);

}