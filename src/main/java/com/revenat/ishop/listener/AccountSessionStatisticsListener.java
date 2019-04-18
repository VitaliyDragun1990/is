package com.revenat.ishop.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.revenat.ishop.config.Constants;

@WebListener
public class AccountSessionStatisticsListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession newSession = se.getSession();
		newSession.setAttribute(Constants.ACCOUNT_REQUEST_STATISTICS, new ArrayList<String>());
		newSession.setMaxInactiveInterval(Constants.SESSION_MAX_INACTIVE_INTERVAL);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		List<String> userRequests = (List<String>) session.getAttribute(Constants.ACCOUNT_REQUEST_STATISTICS);
		String logString = buildLogString(userRequests);
		log(session.getId(), logString);
	}

	private void log(String sessionId, String logString) {
		System.out.println(sessionId + "->\n\t" + logString);
		
	}

	private String buildLogString(List<String> userRequests) {
		return String.join("\n\t", userRequests);
	}
}
