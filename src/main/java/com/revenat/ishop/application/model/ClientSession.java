package com.revenat.ishop.application.model;

import java.io.Serializable;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.domain.model.ShoppingCart;

/**
 * This component holds information about particular application client (in
 * particular their shopping cart and account if such client is logged in).
 * 
 * @author Vitaly Dragun
 *
 */
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

	@Override
	public String toString() {
		return String.format("ClientSession [shoppingCart=%s, account=%s]", shoppingCart, account);
	}
	
}
