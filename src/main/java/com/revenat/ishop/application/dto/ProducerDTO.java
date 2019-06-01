package com.revenat.ishop.application.dto;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Producer;

public class ProducerDTO extends BaseDTO<Integer, Producer> {
	private String name;
	private Integer productCount;
	
	public ProducerDTO() {
	}

	public ProducerDTO(String name, Integer productCount) {
		this.name = name;
		this.productCount = productCount;
	}

	public String getName() {
		return name;
	}

	public Integer getProductCount() {
		return productCount;
	}
}
