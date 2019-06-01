package com.revenat.ishop.ui.controller.page;

import static com.revenat.ishop.ui.config.Constants.Page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.ui.config.Constants.URL;
import com.revenat.ishop.ui.controller.AbstractController;
import com.revenat.ishop.ui.util.RoutingUtils;

public class ShoppingCartController extends AbstractController {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ShoppingCart shoppingCart = getClientSession(req).getShoppingCart();
		if (shoppingCart.isEmpty()) {
			RoutingUtils.redirect(URL.ALL_PRODUCTS, resp);
		} else {
			RoutingUtils.forwardToPage(Page.SHOPPING_CART, req, resp);
		}
	}
}
