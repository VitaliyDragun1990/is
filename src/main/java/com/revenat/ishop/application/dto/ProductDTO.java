package com.revenat.ishop.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.transform.Transformable;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.CommonUtil;

public class ProductDTO implements Transformable<Product>, Serializable {
	private static final long serialVersionUID = 4328294218680349054L;
	
	private Integer id;
	private String name;
	private String description;
	private String imageLink;
	private BigDecimal price;
	private String category;
	private String producer;
	
	public ProductDTO() {
		price = BigDecimal.ZERO;
	}
	
	@Override
	public void transform(Product product, Transformer tarnsformer) {
		this.id = product.getId();	
	}
	
	@Override
	public Product untransform(Product product, Transformer tarnsformer) {
		product.setId(id);
		return product;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	
	@Override
	public String toString() {
		return CommonUtil.toString(this);
	}
	
}
