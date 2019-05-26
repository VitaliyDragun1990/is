package com.revenat.ishop.domain.entity;

import com.revenat.ishop.infrastructure.framework.annotation.jdbc.Column;

public class Account extends AbstractEntity<Integer> {
	private static final long serialVersionUID = -8951792395607124164L;

	private String name;
	private String email;
	@Column("avatar_url")
	private String avatarUrl;
	
	public Account() {
	}
	
	public Account(String name, String email, String avatarUrl) {
		this.name = name;
		this.email = email;
		this.avatarUrl = avatarUrl;
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
	
	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	@Override
	public String toString() {
		return String.format("Account [id=%s, name=%s, email=%s]",getId(), name, email);
	}
}
