package com.revenat.ishop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.revenat.ishop.config.Constants;
import com.revenat.ishop.entity.Product;
import com.revenat.ishop.exception.ConfigurationException;
import com.revenat.ishop.exception.ValidationException;

/**
 * This component represents client's shopping cart. It can store at most
 * {@code 20} different products, and each product unit count can be from 1 to
 * 10 inclusive.
 * 
 * @author Vitaly Dragun
 *
 */
/**
 * @author Vitaly Dragun
 *
 */
public class ShoppingCart implements Serializable {
	private static final long serialVersionUID = 1L;

	private final HashMap<Integer, ShoppingCartItem> cart = new LinkedHashMap<>();
	private int totalCount = 0;
	private BigDecimal totalCost = BigDecimal.ZERO;

	/**
	 * Returns total number of product units in shopping cart.
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * Returns total cost of all products inside this shopping cart instance.
	 */
	public BigDecimal getTotalCost() {
		return totalCost;
	}

	/**
	 * Adds product represented by provided {@code productId} with specified
	 * {@code quantity} to client's shopping cart.
	 * 
	 * @param productId special identifier that represents certain product
	 * @param quantity  number of product units to add
	 * @throws ConfigurationException if at least one of the following is true:
	 *                              quantity to add is less then
	 *                              {@value #MIN_PRODUCT_QUANTITY} or greater than
	 *                              {@value #MAX_PRODUCT_QUANTITY}, total product
	 *                              unit count after adding specified
	 *                              {@code quantity} would be greater than
	 *                              {@value #MAX_PRODUCT_QUANTITY}
	 */
	public void addProduct(Product product, int quantity) {
		validateProductQuantity(quantity);

		ShoppingCartItem oldItem = cart.get(product.getId());
		if (oldItem != null) {
			int totalQuantity = oldItem.getQuantity() + quantity;
			validateTotalProductQuantity(totalQuantity);
			cart.put(product.getId(), new ShoppingCartItem(product, oldItem.getQuantity() + quantity));
		} else {
			validateTotalCartCapacity();
			cart.put(product.getId(), new ShoppingCartItem(product, quantity));
		}
		recalculateTotalCount();
		recalculateTotalCost();
	}

	/**
	 * Removes number of product units from client's shopping cart. If no product
	 * with such {@code productId} in the shopping cart or quantity to remove =
	 * {@code 0}, then do nothing. If specified {@code quantity} greater than number
	 * of product units stored in the shopping cart, then removes product
	 * completely.
	 * 
	 * @param productId special identifier that represents certain product
	 * @param quantity  number of product units to remove
	 * @throws ConfigurationException if quantity to remove is negative.
	 */
	public void removeProduct(int productId, int quantity) {
		if (quantity < 0) {
			throw new ValidationException(
					"You could not remove negative number of product unit: quantity=" + quantity);
		}

		ShoppingCartItem item = cart.get(productId);
		if (item != null) {
			if (quantity >= item.getQuantity()) {
				cart.remove(productId);
			} else {
				cart.put(productId, new ShoppingCartItem(item.getProduct(), item.getQuantity() - quantity));
			}
			recalculateTotalCount();
			recalculateTotalCost();
		}
	}

	/**
	 * Returns unmodifiable collection of {@link ShoppingCartItem}s. If shopping
	 * cart content has been modified after getting such collection, then this
	 * collection is no longer represents actual shopping cart content and new
	 * collection should be retrieved to observe actual shopping cart state.
	 * 
	 * @return {@link Collection} of {@link ShoppingCartItem} instances.
	 */
	public Collection<ShoppingCartItem> getItems() {
		return Collections.unmodifiableCollection(cart.values());
	}
	
	/**
	 * Recalculates shopping cart total cost.
	 */
	public void recalculateTotalCost() {
		totalCost = BigDecimal.ZERO;
		for (ShoppingCartItem item : cart.values()) {
			totalCost = totalCost.add(item.getCost());
		}
	}
	
	public boolean isEmpty() {
		return totalCount == 0;
	}

	@Override
	public String toString() {
		return String.format("ShoppingCart [cart=%s, totalCount=%s, totalCost=%s]", cart, totalCount, totalCost);
	}

	private void validateTotalCartCapacity() {
		if (cart.size() >= Constants.MAX_PRODUCTS_PER_SHOPPING_CART) {
			throw new ValidationException(String.format("Shopping cart cannot store more than %d different products",
					Constants.MAX_PRODUCTS_PER_SHOPPING_CART));
		}
	}

	private static void validateProductQuantity(int quantity) {
		if (quantity < 1 || quantity > Constants.MAX_PRODUCT_COUNT_PER_SHOPPING_CART) {
			throw new ValidationException(String.format("valid product quantity should be between 1 and %d inclusive.",
					Constants.MAX_PRODUCT_COUNT_PER_SHOPPING_CART));
		}
	}
	
	private static void validateTotalProductQuantity(int totalQuantity) {
		if (totalQuantity > Constants.MAX_PRODUCT_COUNT_PER_SHOPPING_CART) {
			throw new ValidationException(String.format("Limit for product count reached: %d.",
					 Constants.MAX_PRODUCT_COUNT_PER_SHOPPING_CART));
		}
	}

	private void recalculateTotalCount() {
		totalCount = cart.values().stream().mapToInt(ShoppingCartItem::getQuantity).sum();
	}

	/**
	 * This unmodifiable component represents shopping cart item which consist of
	 * productId and quantity.
	 * 
	 * @author Vitaly Dragun
	 *
	 */
	public static class ShoppingCartItem implements Serializable {
		private static final long serialVersionUID = 1L;

		private final Product product;
		private final int quantity;

		private ShoppingCartItem(Product product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}

		public Product getProduct() {
			return product;
		}

		public int getQuantity() {
			return quantity;
		}
		
		public BigDecimal getCost() {
			return product.getPrice().multiply(BigDecimal.valueOf(quantity));
		}

		@Override
		public String toString() {
			return String.format("[product=%s, quantity=%s]", product, quantity);
		}
	}
}
