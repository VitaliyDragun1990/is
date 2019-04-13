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
@WebServlet("/hello-world")
public class HelloWorldServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String attr1 = (String) request.getAttribute("attr1");
		if (attr1 == null) {
			attr1 = request.getParameter("attr1");
			if (attr1 == null) {
				attr1 = (String) request.getSession().getAttribute("attr1");
				request.getSession().removeAttribute("attr1");
			}
		}
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		writer.println("<html><body>");
		writer.print("Hello world: " + (attr1 != null ? attr1 : ""));
		writer.println("</body></html>");
		writer.close();
	}

	@Override
	public void destroy() {
		super.destroy();
	}
}
