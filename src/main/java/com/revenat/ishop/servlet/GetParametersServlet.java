package com.revenat.ishop.servlet;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetParametersServlet extends HttpServlet {
	private static final long serialVersionUID = -1380333947337697674L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String param1 = req.getParameter("param1");
		int param2 = Integer.parseInt(req.getParameter("param2"));
		boolean param3 = Boolean.parseBoolean(req.getParameter("param3"));
		String[] param4 = req.getParameterValues("param4");
		// process parameters
		System.out.println(String.format("param1=%s, param2=%d, param3=%s, param4=%s",
				param1, param2, param3, Arrays.toString(param4)));
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
