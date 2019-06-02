package com.revenat.ishop.ui.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.revenat.ishop.application.dto.ClientAccount;
import com.revenat.ishop.application.model.ClientSession;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.ui.config.Constants.Attribute;

/**
 * Responsible for creating new {@link ClientSession} object with empty
 * {@link ShoppingCart} and without {@link ClientAccount} and place it inside
 * each new {@link HttpSession} object created by servlet container for each
 * user of the application.
 * 
 * @author Vitaly Dragun
 *
 */
@WebListener
public class NewClientSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession newSession = se.getSession();
		newSession.setAttribute(Attribute.CLIENT_SESSION, new ClientSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		// do nothing
	}
}
