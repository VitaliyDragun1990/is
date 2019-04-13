package com.revenat.ishop.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HelloWorldServlet
 */
@WebServlet("/hello-world-again")
public class HelloWorldServlet2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String attr1 = (String) request.getAttribute("attr1");
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.print("Hello world again: " + (attr1 != null ? attr1 : ""));
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
