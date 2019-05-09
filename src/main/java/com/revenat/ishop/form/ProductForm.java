package com.revenat.ishop.form;

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

	public ProductForm(int productId, int quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public int getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

}
