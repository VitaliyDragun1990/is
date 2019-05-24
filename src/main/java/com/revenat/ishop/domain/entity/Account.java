package com.revenat.ishop.domain.entity;

import com.revenat.ishop.application.model.ClientAccount;

public class Account extends AbstractEntity<Integer> implements ClientAccount {
	private static final long serialVersionUID = -8951792395607124164L;

	private String name;
	private String email;
	
	public Account() {
	}
	
	public Account(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getDescription() {
		return String.format("%s(%s)", name, email);
	}

	@Override
	public String toString() {
		return String.format("Account [id=%s, name=%s, email=%s]",getId(), name, email);
	}
}
