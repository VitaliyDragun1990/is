package com.revenat.ishop.application.dto;

import java.io.Serializable;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Category;

public class CategoryDTO extends BaseDTO<Integer, Category> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String url;
	private Integer productCount;


	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public Integer getProductCount() {
		return productCount;
	}
}
