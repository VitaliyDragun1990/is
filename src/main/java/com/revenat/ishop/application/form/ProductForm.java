package com.revenat.ishop.application.form;

/**
 * This component represents form which user fills in when adding product to shopping
 * cart.
 * 
 * @author Vitaly Dragun
 *
 */
public class ProductForm {
	private final int productId;
	private final int quantity;

	public ProductForm(String productId, String quantity) {
		this.productId = Integer.parseInt(productId);
		this.quantity = Integer.parseInt(quantity);
	}

	public int getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}
}
