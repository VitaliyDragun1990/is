package com.revenat.ishop.application.dto;

import java.math.BigDecimal;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.application.transform.annotation.Ignore;
import com.revenat.ishop.application.transform.transformer.Transformer;
import com.revenat.ishop.domain.entity.OrderItem;

public class OrderItemDTO extends BaseDTO<Long, OrderItem> {
	private Long orderId;
	@Ignore
	private ProductDTO product;
	private Integer quantity;
	@Ignore
	private BigDecimal cost;
	
	@Override
	public void transform(OrderItem entity, Transformer transformer) {
		this.product = transformer.transform(entity.getProduct(), ProductDTO.class);
		this.cost = product.getPrice().multiply(BigDecimal.valueOf(quantity));
		super.transform(entity, transformer);
	}

	public Long getOrderId() {
		return orderId;
	}
	
	public BigDecimal getCost() {
		return cost;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
