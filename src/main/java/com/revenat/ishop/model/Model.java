package com.revenat.ishop.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Model implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> list = Arrays.asList("first","second", "third");
	
	public List<String> getList() {
		return list;
	}
	
	public String getData() {
		StringBuilder sb = new StringBuilder();
		for (String item : list) {
			sb.append(item).append(" || ");
		}
		return sb.toString();
	}
}
