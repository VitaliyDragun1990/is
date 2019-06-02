package com.revenat.ishop.application.model;

import java.io.Serializable;
import java.util.Locale;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.infrastructure.util.CommonUtil;

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
	private boolean isCartUpdated;
	private Locale clientLocale;
	private ShoppingCartEventListener listener = new ClientSessionCartEventListener();

	/**
	 * Creates new client session with empty shopping cart and {@code null} client
	 * account/
	 */
	public ClientSession() {
		this.shoppingCart = new ShoppingCart();
		this.isCartUpdated = false;
		this.shoppingCart.addEventListener(listener);
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart.removeEventListener(listener);
		this.shoppingCart = shoppingCart;
		this.shoppingCart.addEventListener(listener);
	}

	public ClientAccount getAccount() {
		return account;
	}

	public void setAccount(ClientAccount account) {
		this.account = account;
	}
	
	public boolean isCartUpdated() {
		return isCartUpdated;
	}
	
	public void setCartUpdated(boolean isCartUpdated) {
		this.isCartUpdated = isCartUpdated;
	}
	
	public Locale getClientLocale() {
		return clientLocale;
	}
	
	public void setClientLocale(Locale clientLocale) {
		this.clientLocale = clientLocale;
	}

	@Override
	public String toString() {
		return CommonUtil.toString(this);
	}
	
	private class ClientSessionCartEventListener implements ShoppingCartEventListener {
		private static final long serialVersionUID = -8973747964755338591L;

		@Override
		public void productWasAdded(ProductDTO product, int quantity) {
			isCartUpdated = true;
			
		}

		@Override
		public void productWasRemoved(ProductDTO product, int quantity) {
			isCartUpdated = true;
			
		}
		
		@Override
		public void cartWasCleared() {
			isCartUpdated = true;
		}
	}
}
