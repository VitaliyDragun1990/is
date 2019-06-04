package com.revenat.ishop.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="category")
public class Category extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -1834996509365201052L;
	
	@Column
	private String name;
	@Column
	private String url;
	@Column(name = "product_count")
	private Integer productCount;
	
	public Category() {
	}

	public Category(Integer id, String name, String url) {
		setId(id);
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public int getProductCount() {
		return productCount;
	}
	
	@Override
	public void setId(Integer id) {
		super.setId(id);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	@Override
	public String toString() {
		return String.format("Category [id=%s, name=%s, url=%s, productCount=%s]", getId(), name, url, productCount);
	}
}
