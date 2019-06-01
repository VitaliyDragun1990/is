package com.revenat.ishop.application.dto;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Category;

public class CategoryDTO extends BaseDTO<Integer, Category> {
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
