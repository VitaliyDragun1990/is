package com.revenat.ishop.ui.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.ui.config.Constants.Attribute;

public abstract class AbstractController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	protected final ClientSession getClientSession(HttpServletRequest request) {
		return (ClientSession) request.getSession().getAttribute(Attribute.CLIENT_SESSION);
	}
}
