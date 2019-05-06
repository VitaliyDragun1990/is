package com.revenat.ishop.service.application;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.entity.Product;
import com.revenat.ishop.exception.ValidationException;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.repository.ProductRepository;
import com.revenat.ishop.repository.ShoppingCartRepository;

/**
 * This component incapsulates CRUD shopping cart logic. It contains various
 * methods for retrieving, updating, setting, deleting client's
 * {@link ShoppingCart} object.
 * 
 * @author Vitaly Dragun
 *
 */
public class ShoppingCartService {
	private final ShoppingCartRepository cartRepository;
	private final ProductRepository productRepository;

	public ShoppingCartService(ShoppingCartRepository cartRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
	}

	/**
	 * Returns {@link ShoppingCart} instance associated with provided client's
	 * {@link HttpSession} object.
	 * 
	 * @param clientSession {@link HttpSession} object that represents serverside
	 *                      session for particular client.
	 * @return {@link ShoppingCart} instance associated with particular client.
	 */
	public ShoppingCart getShoppingCart(HttpSession clientSession) {
		return cartRepository.getShoppingCart(clientSession);
	}

	/**
	 * Persists provided shopping cart content as cookie string and store it inside
	 * provied {@link HttpServletResponse} object. If provided cart is empty one,
	 * then such cookie would be deleted.
	 * 
	 * @param shoppingCart {@link ShoppingCart} instance to persist as cookie
	 * @param response     {@link HttpServletResponse} object that will store such
	 *                     shopping cart cookie.
	 */
	public void persistShoppingCart(ShoppingCart shoppingCart, HttpServletResponse response) {
		if (shoppingCart.getTotalCount() == 0) {
			cartRepository.removeShoppingCartCookie(response);
		} else {
			cartRepository.persistShoppingCartAsCookie(shoppingCart, response);
		}
	}

	/**
	 * Clears shopping cart instance (if any) associated with provided user's
	 * {@link HttpSession} object. Deletes 'shopping cart' cookie using provided
	 * {@link HttpServletResponse} object.
	 * 
	 * @param userSession {@link HttpSession} object that represents serverside
	 *                    session for particular user.
	 * @param response    {@link HttpServletResponse} object that may store shopping
	 *                    cart cookie.
	 */
	public void clearShoppingCart(HttpSession userSession, HttpServletResponse response) {
		cartRepository.setShoppingCart(userSession, new ShoppingCart());
		cartRepository.removeShoppingCartCookie(response);
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
