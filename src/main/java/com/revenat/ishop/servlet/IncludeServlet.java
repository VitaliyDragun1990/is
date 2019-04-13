package com.revenat.ishop.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/include")
public class IncludeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("attr1", "from include servlet");
		PrintWriter writer = resp.getWriter();
		writer.println("<html><body>");
		req.getRequestDispatcher("/hello-world-again").include(req, resp);
		writer.println("!!!");
		writer.println("</body></html>");
		writer.close();
	}
}
