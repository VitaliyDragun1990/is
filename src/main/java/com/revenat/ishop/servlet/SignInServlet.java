package com.revenat.ishop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sign-in")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		
		// some checks here
		
		req.getSession().setAttribute("IS_AUTHENTICATED", true);
		req.getSession().setAttribute("username", login);
		String cameFrom = (String) req.getSession().getAttribute("REDIRECT_URL_AFTER_SIGNIN");
		
		resp.sendRedirect(cameFrom);
	}
}
