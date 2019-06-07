package com.revenat.ishop.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.revenat.ishop.application.transform.Transformable;
import com.revenat.ishop.application.transform.annotation.Ignore;
import com.revenat.ishop.application.transform.transformer.Transformer;
import com.revenat.ishop.application.util.CommonUtil;
import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.entity.Product;

public class ProductDTO implements Transformable<Product>, Serializable {
	private static final long serialVersionUID = 4328294218680349054L;
	
	private Integer id;
	private String name;
	private String description;
	private String imageLink;
	private BigDecimal price;
	@Ignore
	private CategoryDTO category;
	@Ignore
	private ProducerDTO producer;
	
	public ProductDTO() {
		price = BigDecimal.ZERO;
	}
	
	@Override
	public void transform(Product product, Transformer transformer) {
		this.id = product.getId();
		this.category = transformer.transform(product.getCategory(), CategoryDTO.class);
		this.producer = transformer.transform(product.getProducer(), ProducerDTO.class);
	}
	
	@Override
	public Product untransform(Product product, Transformer transformer) {
		product.setId(id);
		product.setCategory(transformer.untransform(category, Category.class));
		product.setProducer(transformer.untransform(producer, Producer.class));
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
		return category.getName();
	}

	public String getProducer() {
		return producer.getName();
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return CommonUtil.toString(this);
	}
	
}
