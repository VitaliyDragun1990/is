package com.revenat.ishop.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product extends AbstractEntity<Integer> {
	private static final long serialVersionUID = 2900041021842951941L;
	
	@Column
	private String name;
	@Column
	private String description;
	@Column(name = "image_link")
	private String imageLink;
	@Column
	private BigDecimal price;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "producer_id", nullable = false)
	private Producer producer;
	
	public Product() {
		price = BigDecimal.ZERO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	@Override
	public String toString() {
		return String.format(
				"Product [id=%s, name=%s, description=%s, imageLink=%s, price=%s, category=%s, producer=%s]", getId(),
				name, description, imageLink, price, category, producer);
	}
}
