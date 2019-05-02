package com.revenat.ishop.servlet.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.revenat.ishop.form.ProductForm;
import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.servlet.AbstractController;
import com.revenat.ishop.util.web.RoutingUtils;

public abstract class AbstractProductController extends AbstractController {
	private static final long serialVersionUID = -2501655667072837803L;
	private static final String TOTAL_COST = "totalCost";
	private static final String TOTAL_COUNT = "totalCount";

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductForm form = getProductForm(req);
		ShoppingCart cart = getShoppingCartService().getShoppingCart(req.getSession());
		processProductForm(form, cart, resp);
		sendResponse(cart, resp);
	}

	protected void sendResponse(ShoppingCart cart, HttpServletResponse resp) throws IOException {
		String json = buildJsonResponse(cart);
		RoutingUtils.sendJSON(json, resp);
	}
	
	protected String buildJsonResponse(ShoppingCart shoppingCart) {
		JSONObject response = new JSONObject();
		response.put(TOTAL_COUNT, shoppingCart.getTotalCount());
		response.put(TOTAL_COST, shoppingCart.getTotalCost());
		return response.toString();
	}

	protected abstract void processProductForm(ProductForm form, ShoppingCart cart, HttpServletResponse resp);
}
