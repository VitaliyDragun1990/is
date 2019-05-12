package com.revenat.ishop.application.domain.entity;

public class Producer extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -4130535481455511674L;
	
	private String name;
	private int productCount;
	
	public Producer() {
	}

	public Producer(Integer id, String name) {
		setId(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	@Override
	public String toString() {
		return String.format("Producer [id=%s, name=%s, productCount=%s]", getId(), name, productCount);
	}
}
