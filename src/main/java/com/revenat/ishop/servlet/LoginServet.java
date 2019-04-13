package com.revenat.ishop.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		try (PrintWriter writer = resp.getWriter()) {
			if (req.getAttribute("error") != null) {
				writer.println("<h5 style='color:red'>" + req.getAttribute("error") + "</h5>");
			}
			writer.print("<form action='/login' method='post'>");
			writer.print("<input name='login' placeholder='Login'>");
			writer.print("<input name='password' placeholder='Password' type='password'>");
			writer.print("<input type='submit' value='Login'>");
			writer.println("</form>");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		
		if (isValid(req, login, password)) {
			req.getSession().setAttribute("username", login);
//			req.getRequestDispatcher("/WEB-INF/jsp/my-private-page.jsp").forward(req, resp);
			resp.sendRedirect("/my-private-page");
		} else {
			doGet(req, resp);
		}
	}

	private boolean isValid(HttpServletRequest req, String login, String password) {
		if (login == null || login.trim().isEmpty()) {
			req.setAttribute("error", "Login is required");
			return false;
		}
		if (password == null || password.trim().isEmpty()) {
			req.setAttribute("error", "Password is required");
			return false;
		}
		return true;
	}
}
