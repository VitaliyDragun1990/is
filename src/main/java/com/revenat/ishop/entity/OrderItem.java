package com.revenat.ishop.entity;

import java.math.BigDecimal;

public class OrderItem extends AbstractEntity<Long> {
	private static final long serialVersionUID = -4694344400417048155L;
	
	private Long orderId;
	private Product product;
	private int quantity;
	
	public OrderItem() {
	}

	public OrderItem(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public BigDecimal getCost() {
		return product.getPrice().multiply(BigDecimal.valueOf(quantity));
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return String.format("OrderItem [id=%s, orderId=%s, product=%s, quantity=%s]", getId(), orderId, product, quantity);
	}
}
