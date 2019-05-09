package com.revenat.ishop.model;

import java.io.Serializable;

public class ClientSession implements Serializable {
	private static final long serialVersionUID = -3440928401701044463L;
	
	private ShoppingCart shoppingCart;
	private ClientAccount account;

	/**
	 * Creates new client session with empty shopping cart and {@code null} client
	 * account/
	 */
	public ClientSession() {
		this.shoppingCart = new ShoppingCart();
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}
}
