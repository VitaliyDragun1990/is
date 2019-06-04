package com.revenat.ishop.application.dto;

import java.io.Serializable;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Producer;

public class ProducerDTO extends BaseDTO<Integer, Producer> implements Serializable {
	private static final long serialVersionUID = -3823429345326396480L;
	
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
