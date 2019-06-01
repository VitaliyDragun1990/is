package com.revenat.ishop.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Order;
import com.revenat.ishop.infrastructure.transform.annotation.Ignore;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.CommonUtil;

public class OrderDTO extends BaseDTO<Long, Order> {
	private Integer accountId;
	@Ignore
	private List<OrderItemDTO> items;
	private LocalDateTime created;
	private BigDecimal totalCost;
	
	@Override
	public void transform(Order entity, Transformer transformer) {
		items = transformer.transfrom(entity.getItems(), OrderItemDTO.class);
		totalCost = calculateTotalCost();
		super.transform(entity, transformer);
	}
	
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	
	private BigDecimal calculateTotalCost() {
		BigDecimal total = BigDecimal.ZERO;
		for (OrderItemDTO item : items) {
			total = total.add(item.getCost());
		}
		return total;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public List<OrderItemDTO> getItems() {
		return CommonUtil.getSafeList(items);
	}

	public void setItems(List<OrderItemDTO> items) {
		this.items = items;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
}
