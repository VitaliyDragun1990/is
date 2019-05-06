package com.revenat.ishop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order extends AbstractEntity<Long> {
	private static final long serialVersionUID = 4243683635737044229L;

	private Integer accountId;
	private List<OrderItem> items;
	private LocalDateTime created;
	
	public Order() {
		items = new ArrayList<>();
		created = LocalDateTime.now();
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public LocalDateTime getCreated() {
		return created;
	}
	
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public List<OrderItem> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	public void setItems(List<OrderItem> items) {
		this.items.addAll(items);
	}
	
	public void addItem(OrderItem item) {
		items.add(item);
	}
	
	public BigDecimal getTotalCost() {
		BigDecimal total = BigDecimal.ZERO;
		for (OrderItem item : items) {
			total = total.add(item.getCost());
		}
		return total;
	}

	@Override
	public String toString() {
		return String.format("Order [id=%s, accountId=%s, items=%s, created=%s]",getId(), accountId, items, created);
	}
}
