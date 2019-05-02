package com.revenat.ishop.servlet.page;

import static com.revenat.ishop.config.Constants.Page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public class ShoppingCartController extends AbstractController {
	private static final long serialVersionUID = 1L;
	private static final String PRODUCTS = "/products";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ShoppingCart shoppingCart = getShoppingCartService().getShoppingCart(req.getSession());
		if (shoppingCart.isEmpty()) {
			RoutingUtils.redirect(PRODUCTS, resp);
		} else {
			RoutingUtils.forwardToPage(Page.SHOPPING_CART, req, resp);
		}
	}
}
