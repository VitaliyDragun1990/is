package com.revenat.ishop.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cookie")
public class CookieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cookie cookie = new Cookie("author", "devstudy");
		cookie.setMaxAge(1800);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		
		resp.addCookie(cookie);
		
		resp.setContentType("text/html");
		PrintWriter writer = resp.getWriter();
		writer.println("<h2>Cookie Page</h2>");
		writer.flush();
		writer.close();
	}
}
