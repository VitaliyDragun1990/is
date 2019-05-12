package com.revenat.ishop.presentation.listener;

import java.util.List;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.revenat.ishop.presentation.infra.config.Constants.Attribute;
import com.revenat.ishop.presentation.infra.util.WebUtils;

/**
 * This request listener responsible for storing clients request details
 * (method, url) into clients' sessions.
 * 
 * @author Vitaly Dragun
 *
 */
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
		String requestUrl = WebUtils.getCurrentRequestUrl(request);

		return String.format("%s %s", method, requestUrl);
	}

	@SuppressWarnings("unchecked")
	static List<String> getUserRequests(HttpSession session) {
		return (List<String>) session.getAttribute(Attribute.ACCOUNT_REQUEST_STATISTICS);
	}

}
