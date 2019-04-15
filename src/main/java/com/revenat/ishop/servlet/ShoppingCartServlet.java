package com.revenat.ishop.servlet;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.model.ShoppingCart;
import com.revenat.ishop.util.ShoppingCartMapper;
import com.revenat.ishop.util.ShoppingCartUtils;

public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final ShoppingCartMapper cookieMapper;

	public ShoppingCartServlet(ShoppingCartMapper cookieMapper) {
		this.cookieMapper = cookieMapper;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cmd = req.getParameter("cmd");
		if ("clear".equals(cmd)) {
			ShoppingCartUtils.removeCurrentShoppingCart(req.getSession(), resp);
		} else if ("invalidate".equals(cmd)) {
			req.getSession().invalidate();
		} else if ("add".equals(cmd)) {
			addProduct(req, resp);
		} else {
			synchronizeShoppingCart(req, resp);
		}
		showShoppingCart(req, resp);
	}
	
	private void addProduct(HttpServletRequest req, HttpServletResponse resp) {
		ShoppingCart cart = ShoppingCartUtils.getCurrentShoppingCart(req.getSession());
		ThreadLocalRandom random = ThreadLocalRandom.current();
		cart.addProduct(random.nextInt(1, 10), random.nextInt(1, 3));
		
	}

	private void synchronizeShoppingCart(HttpServletRequest req, HttpServletResponse resp) {
		if (ShoppingCartUtils.isCurrentShoppingCartExists(req.getSession())) {
			ShoppingCart cart = ShoppingCartUtils.getCurrentShoppingCart(req.getSession());
			String cookieValue = cookieMapper.toString(cart);
			ShoppingCartUtils.setCurrentShoppingCartCookie(cookieValue, resp);
		} else if (ShoppingCartUtils.findShoppingCartCookie(req) != null) {
			Cookie cartCookie = ShoppingCartUtils.findShoppingCartCookie(req);
			ShoppingCart cart = cookieMapper.fromString(cartCookie.getValue());
			ShoppingCartUtils.setCurrentShoppingCart(req.getSession(), cart);
		}
	}
	
	private void showShoppingCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		if (ShoppingCartUtils.isCurrentShoppingCartExists(session)) {
			resp.getWriter().println(ShoppingCartUtils.getCurrentShoppingCart(session));
		} else {
			resp.getWriter().println("Shopping Cart is not exist");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
