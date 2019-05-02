package com.revenat.ishop.servlet.ajax;

import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.form.ProductForm;
import com.revenat.ishop.model.ShoppingCart;

public class AddProductToShoppingCartController extends AbstractProductController {
	private static final long serialVersionUID = -8374463718109105746L;

	@Override
	protected void processProductForm(ProductForm form, ShoppingCart cart, HttpServletResponse resp) {
		getShoppingCartService().addProductToShoppingCart(form.getProductId(), form.getQuantity(), cart);
		getShoppingCartService().persistShoppingCart(cart, resp);
	}
}
