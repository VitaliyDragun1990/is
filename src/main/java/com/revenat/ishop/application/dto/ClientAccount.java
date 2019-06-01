package com.revenat.ishop.application.dto;

import com.revenat.ishop.application.dto.base.BaseDTO;
import com.revenat.ishop.domain.entity.Account;

/**
 * This interface represents currently logged in user in the application.
 * 
 * @author Vitaly Dragun
 *
 */
public class ClientAccount extends BaseDTO<Integer, Account> {
	private String name;
	private String email;
	private String avatarUrl;
	
	public ClientAccount() {
	}

	public ClientAccount(int id, String name, String email, String avatarUrl) {
		setId(id);
		this.name = name;
		this.email = email;
		this.avatarUrl = avatarUrl;
	}
	
	public String getDescription() {
		return String.format("%s(%s)", name, email);
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
	
}
