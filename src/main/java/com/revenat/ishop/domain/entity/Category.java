package com.revenat.ishop.domain.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Column;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.entity.Table;

@Table(name="category")
@XmlRootElement(name ="category")
public class Category extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -1834996509365201052L;
	
	private String name;
	private String url;
	@Column("product_count")
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
	

	@XmlAttribute
	@Override
	public void setId(Integer id) {
		super.setId(id);
	}

	@XmlAttribute
	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
	}

	@XmlAttribute(name="product-count")
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	@Override
	public String toString() {
		return String.format("Category [id=%s, name=%s, url=%s, productCount=%s]", getId(), name, url, productCount);
	}
}
