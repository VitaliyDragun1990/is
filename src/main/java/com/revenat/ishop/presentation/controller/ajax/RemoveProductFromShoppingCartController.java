package com.revenat.ishop.presentation.controller.ajax;

import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.form.ProductForm;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.application.service.ShoppingCartService;

public class RemoveProductFromShoppingCartController extends AbstractShoppingCartController {
	private static final long serialVersionUID = -8374463718109105746L;

	public RemoveProductFromShoppingCartController(ShoppingCartService shoppingCartService) {
		super(shoppingCartService);
	}
	
	@Override
	protected void processProductForm(ProductForm form, ShoppingCart cart, HttpServletResponse resp) {
		shoppingCartService.removeProductFromShoppingCart(form.getProductId(), form.getQuantity(), cart);
	}
}
