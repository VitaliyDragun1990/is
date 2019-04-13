package com.revenat.ishop.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String MSG_OK = "<h1>OK</h1>";
	private static final String MSG_FAILED = "<h1>FAILED</h1>";
	
	private static String ip;
	private static String accessKey;
	private static String login;
	private static String password;
	
	@Override
	public void init() throws ServletException {
		ip = getServletConfig().getInitParameter("ip");
		accessKey = getServletConfig().getInitParameter("accessKey");
		login = getServletConfig().getInitParameter("login");
		password = getServletConfig().getInitParameter("password");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String clientIP = req.getRemoteAddr();
		String accessKeyHeaderValue = req.getHeader("ACCESS_KEY");
		String clientLogin = req.getParameter("login");
		String clientPassword = req.getParameter("password");
		
		try (PrintWriter writer = resp.getWriter()) {
			String responseMsg = validateLoginData(clientIP, accessKeyHeaderValue, clientLogin, clientPassword);
			
			if (MSG_FAILED.equals(responseMsg)) {
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
			
			writer.write(responseMsg);
			writer.flush();
		} catch (IOException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	private String validateLoginData(String clientIP, String clientAccessKey, String clientLogin, String clientPassword) {
		if (ip.equals(clientIP)) {
			logToConsole(String.format("Login via ip: %s", clientIP));
			return MSG_OK;
		} else if (accessKey.equals(clientAccessKey)) {
			logToConsole(String.format("Login via accessKey: %s", clientAccessKey));
			return MSG_OK;
		} else if (login.equals(clientLogin) && password.equals(clientPassword)) {
			logToConsole(String.format("Login via login and password: %s/%s", clientLogin, clientPassword));
			return MSG_OK;
		} else {
			logToConsole(String.format("Client login parameters: IP=[%s], accessKey=[%s], login=[%s], password=[%s]",
					clientIP, clientAccessKey, clientLogin, clientPassword));
			return MSG_FAILED;
		}
	}
	
	private static void logToConsole(String msg) {
		System.out.println(msg);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
