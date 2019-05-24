package com.revenat.ishop.domain.entity;

public class Category extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -1834996509365201052L;
	
	private String name;
	private String url;
	private int productCount;
	
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
