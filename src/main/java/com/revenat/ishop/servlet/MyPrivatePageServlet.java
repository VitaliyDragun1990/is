package com.revenat.ishop.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/my-private-page")
public class MyPrivatePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = (String) req.getSession().getAttribute("username");
		if (username != null) {
			req.getSession().removeAttribute("username");
			req.getRequestDispatcher("/WEB-INF/jsp/my-private-page.jsp").forward(req, resp);
		} else {
			resp.sendRedirect("/login");
		}
	}
}
