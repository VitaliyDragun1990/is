package com.revenat.ishop.application.service;

import javax.servlet.http.HttpSession;

import com.revenat.ishop.application.domain.model.ClientAccount;
import com.revenat.ishop.application.domain.model.ClientSession;

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
	boolean isAuthenticated(ClientSession session);

	/**
	 * Authenticate user which is represented by specified {@link HttpSession}
	 * instance inside application using provided {@code authToken} parameter.
	 * 
	 * @param credentials user provided credentials.
	 * @param userSession {@link HttpSession} object which represents particular
	 *                    user of the application.
	 */
	void authenticate(Credentials credentials, ClientSession session);

	/**
	 * Returns string URL which user should visit to start authentication process.
	 * 
	 * @return string URL to visit to start authentication process.
	 */
	String getAuthenticationUrl();

	/**
	 * Returns account in form of {@link ClientAccount} of the authenticated user
	 * represented by the provided {@link HttpSession} object.
	 * 
	 * @param userSession {@link HttpSession} object which represents particular
	 *                    user of the application.
	 * @return {@link ClientAccount} object if the user represented by provided
	 *         {@link HttpSession} object is authenticated, or {@code null}
	 *         otherwise.
	 */
	ClientAccount getAuthenticatedUserAccount(ClientSession session);

	/**
	 * Logs out client specified by {@link ClientSession} {@code session} parameter.
	 * 
	 * @param session {@link ClientSession} object representing particular client.
	 */
	void logout(ClientSession session);

	/**
	 * Represents user-provided credentials for authentication purpose.
	 * 
	 * @author Vitaly Dragun
	 *
	 */
	public interface Credentials {

	}
}