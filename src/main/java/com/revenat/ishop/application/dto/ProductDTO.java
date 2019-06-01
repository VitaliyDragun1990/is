package com.revenat.ishop.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Product;

public class ProductDTO extends BaseDTO<Integer, Product> implements Serializable {
	private static final long serialVersionUID = 4328294218680349054L;
	
	private String name;
	private String description;
	private String imageLink;
	private BigDecimal price;
	private String category;
	private String producer;
	
	public ProductDTO() {
		price = BigDecimal.ZERO;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getImageLink() {
		return imageLink;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getCategory() {
		return category;
	}

	public String getProducer() {
		return producer;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
