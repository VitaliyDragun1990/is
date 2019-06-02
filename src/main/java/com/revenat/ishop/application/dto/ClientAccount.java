package com.revenat.ishop.application.dto;

import java.io.Serializable;

import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.transform.Transformable;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.CommonUtil;

/**
 * This interface represents currently logged in user in the application.
 * 
 * @author Vitaly Dragun
 *
 */
public class ClientAccount implements Transformable<Account>, Serializable {
	private static final long serialVersionUID = -4219199461297332520L;
	
	private Integer id;
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
	
	@Override
	public void transform(Account account, Transformer tarnsformer) {
		this.id = account.getId();
	}
	
	@Override
	public Account untransform(Account account, Transformer tarnsformer) {
		account.setId(id);
		return account;
	}
	
	public String getDescription() {
		return String.format("%s(%s)", name, email);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
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
	
	@Override
		public String toString() {
			return CommonUtil.toString(this);
		}
}
