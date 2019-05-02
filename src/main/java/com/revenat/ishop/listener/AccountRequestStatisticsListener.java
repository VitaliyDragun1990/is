package com.revenat.ishop.listener;

import java.util.List;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.config.Constants.Attribute;

@WebListener
public class AccountRequestStatisticsListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		// do nothing
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
		
		List<String> userRequests = getUserRequests(request.getSession());
		userRequests.add(buildRequestRecord(request));
	}
	
	private String buildRequestRecord(HttpServletRequest request) {
		String method = request.getMethod();
		String requestUrl = request.getRequestURI();
		String queryString = request.getQueryString();
		
		return buildRequestString(method, requestUrl, queryString);
	}

	private String buildRequestString(String method, String requestUrl, String queryString) {
		StringBuilder builder = new StringBuilder();
		builder.append(method).append(" ").append(requestUrl);
		if (queryString != null) {
			builder.append("?").append(queryString);
		}
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	static List<String> getUserRequests(HttpSession session) {
		return (List<String>) session.getAttribute(Attribute.ACCOUNT_REQUEST_STATISTICS);
	}

}
