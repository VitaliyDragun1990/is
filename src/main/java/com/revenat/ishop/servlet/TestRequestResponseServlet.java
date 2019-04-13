package com.revenat.ishop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestRequestResponseServlet
 */
@WebServlet("/test-req-resp")
public class TestRequestResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setDateHeader("Date", System.currentTimeMillis());
		response.addHeader("autor", "devstudy.net");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setLocale(Locale.ITALIAN);
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<form action='/test-req-resp' method='post'>");
		out.println("Your name: <input name='name'><br>");
		out.println("<input type='submit'>");
		out.println("</body></html>");
		out.close();
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(" Request method: "+request.getMethod());
		out.println("<br>Request URI: "+request.getRequestURI());
		out.println("<br>Protocol Version: "+request.getProtocol());
		out.println("<br>Header 'User-Agent': "+request.getHeader("User-Agent"));
		out.println("<br>Header 'Accept-Language': "+request.getLocale());
		out.println("<br>Header 'Content-Length': "+request.getContentLength());
		out.println("<br>Header 'Content-Type': "+request.getContentType());
		out.println("<br>Remote address: "+request.getRemoteHost());
		out.println("<br>Request parameter: "+request.getParameter("name"));
		out.close();
	}

}
