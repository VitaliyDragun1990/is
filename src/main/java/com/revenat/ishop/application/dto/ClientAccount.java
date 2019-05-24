package com.revenat.ishop.application.dto;

import java.io.Serializable;

/**
 * This interface represents currently logged in user in the application.
 * 
 * @author Vitaly Dragun
 *
 */
public class ClientAccount implements Serializable {
	private static final long serialVersionUID = -5588097152703208436L;
	
	private int id;
	private String name;
	private String email;
	private String avatarUrl;
	
	public ClientAccount() {
	}

	public ClientAccount(int id, String name, String email, String avatarUrl) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.avatarUrl = avatarUrl;
	}
	
	public String getDescription() {
		return String.format("%s(%s)", name, email);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
