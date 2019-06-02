package com.revenat.ishop.application.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.OrderItem;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.exception.flow.ValidationException;
import com.revenat.ishop.infrastructure.transform.annotation.Ignore;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.Checks;
import com.revenat.ishop.infrastructure.util.CommonUtil;

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
	private static final long serialVersionUID = 6526125465093002906L;
	
	public static final int MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART = 10;
	public static final int MAX_PRODUCTS_PER_SHOPPING_CART = 20;
	private static final String CART_TOTAL_LIMIT_MSG_CODE = "message.error.cartTotalLimit";
	private static final String CART_PRODUCT_INSTANCE_LIMIT_MSG_CODE = "message.error.cartProductInstanceLimit";

	private final HashMap<Integer, ShoppingCartItem> cart = new LinkedHashMap<>();
	private int totalCount = 0;
	private BigDecimal totalCost = BigDecimal.ZERO;
	private ArrayList<ShoppingCartEventListener> eventListeners = new ArrayList<>();
	
	public void addEventListener(ShoppingCartEventListener listener) {
		eventListeners.add(listener);
	}
	
	public void removeEventListener(ShoppingCartEventListener listener) {
		eventListeners.remove(listener);
	}

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
	 * @throws ValidationException if total product unit count after adding
	 *                             specified {@code quantity} would be greater than
	 *                             {@value #MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART} or
	 *                             cart {@code capacity} greater then
	 *                             {@value #MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART} x
	 *                             {@value #MAX_PRODUCTS_PER_SHOPPING_CART}
	 */
	public void addProduct(ProductDTO product, int quantity) {
		ShoppingCartItem oldItem = cart.get(product.getId());
		if (oldItem != null) {
			int numberOfProducts = oldItem.getQuantity() + quantity;
			validateCartItemNumberOfProducts(numberOfProducts);
			cart.put(product.getId(), new ShoppingCartItem(product, oldItem.getQuantity() + quantity));
		} else {
			validateCartSize(cart.size());
			cart.put(product.getId(), new ShoppingCartItem(product, quantity));
		}
		
		eventListeners.forEach(listener -> listener.productWasAdded(product, quantity));
		
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
	 */
	public void removeProduct(int productId, int quantity) {
		ShoppingCartItem item = cart.get(productId);
		if (item != null) {
			if (quantity >= item.getQuantity()) {
				cart.remove(productId);
			} else {
				cart.put(productId, new ShoppingCartItem(item.getProduct(), item.getQuantity() - quantity));
			}
			
			eventListeners.forEach(listener -> listener.productWasRemoved(item.getProduct(), quantity));
			
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
	public List<ShoppingCartItem> getItems() {
		List<ShoppingCartItem> items = new ArrayList<>();
		items.addAll(cart.values());
		return CommonUtil.getSafeList(items);
	}

	/**
	 * Recalculates shopping cart total cost.
	 */
	private void recalculateTotalCost() {
		totalCost = BigDecimal.ZERO;
		for (ShoppingCartItem item : cart.values()) {
			totalCost = totalCost.add(item.getCost());
		}
	}

	public boolean isEmpty() {
		return totalCount == 0;
	}

	public void clear() {
		this.cart.clear();
		
		eventListeners.forEach(ShoppingCartEventListener::cartWasCleared);
		
		recalculateTotalCost();
		recalculateTotalCount();
	}

	@Override
	public String toString() {
		return String.format("ShoppingCart [cart=%s, totalCount=%s, totalCost=%s]", cart, totalCount, totalCost);
	}

	private static void validateCartSize(int cartSize) {
		Checks.validateCondition(cartSize < MAX_PRODUCTS_PER_SHOPPING_CART,
				"Shopping cart cannot store more than %d different products",
				CART_TOTAL_LIMIT_MSG_CODE,
				MAX_PRODUCTS_PER_SHOPPING_CART);
	}

	private static void validateCartItemNumberOfProducts(int cartItemNumberOfProducts) {
		Checks.validateCondition(cartItemNumberOfProducts <= MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART,
				"Limit for max instances per product reached: %d.",
				CART_PRODUCT_INSTANCE_LIMIT_MSG_CODE,
				MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART);
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
	public static class ShoppingCartItem extends BaseDTO<Long, OrderItem> implements Serializable {
		private static final long serialVersionUID = 104011869244013321L;
		
		@Ignore
		private ProductDTO product;
		private Integer quantity;
		
		public ShoppingCartItem() {
		}
		
		@Override
		public OrderItem untransform(OrderItem entity, Transformer transformer) {
			entity.setProduct(transformer.untransform(product, Product.class));
			
			return super.untransform(entity, transformer);
		}

		private ShoppingCartItem(ProductDTO product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}

		public ProductDTO getProduct() {
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
