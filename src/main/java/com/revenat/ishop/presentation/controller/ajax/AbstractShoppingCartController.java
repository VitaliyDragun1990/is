package com.revenat.ishop.presentation.controller.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.revenat.ishop.application.form.ProductForm;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.presentation.controller.AbstractController;
import com.revenat.ishop.presentation.util.RoutingUtils;

public abstract class AbstractShoppingCartController extends AbstractController {
	private static final long serialVersionUID = -2501655667072837803L;
	private static final String QUANTITY = "quantity";
	private static final String PRODUCT_ID = "productId";
	private static final String TOTAL_COST = "totalCost";
	private static final String TOTAL_COUNT = "totalCount";
	
	protected final ShoppingCartService shoppingCartService;
	
	public AbstractShoppingCartController(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductForm form = getProductForm(req);
		ShoppingCart cart = getClientSession(req).getShoppingCart();
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
	
	
	private ProductForm getProductForm(HttpServletRequest req) {
		return new ProductForm(
				req.getParameter(PRODUCT_ID),
				req.getParameter(QUANTITY));
	}
}
