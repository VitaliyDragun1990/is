package com.revenat.ishop.service;

import javax.servlet.http.HttpSession;

/**
 * This application service is responsible for managing authentication-related
 * logic.
 * 
 * @author Vitaly Dragun
 *
 */
public interface AuthenticationService {

	/**
	 * Checks whether the user of the application represented by {@link HttpSession}
	 * object is currently authenticated.
	 * 
	 * @param userSession {@link HttpSession} object which represents particular
	 *                    user of the application.
	 * @return {@code true} if user is authenticated, {@code false} otherwise.
	 */
	boolean isAuthenticated(HttpSession userSession);

	/**
	 * Authenticate user which is represented by specified {@link HttpSession}
	 * instance inside application using provided {@code authToken} parameter.
	 * 
	 * @param credentials user provided credentials.
	 * @param userSession {@link HttpSession} object which represents particular
	 *                    user of the application.
	 */
	void authenticate(Credentials credentials, HttpSession userSession);

	/**
	 * Returns string URL which user should visit to start authentication process.
	 * 
	 * @return string URL to visit to start authentication process.
	 */
	String getAuthenticationUrl();

}