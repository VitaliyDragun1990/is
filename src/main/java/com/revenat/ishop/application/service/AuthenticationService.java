package com.revenat.ishop.application.service;

import com.revenat.ishop.application.model.ClientAccount;
import com.revenat.ishop.application.model.ClientSession;

/**
 * This application service is responsible for managing authentication-related
 * logic.
 * 
 * @author Vitaly Dragun
 *
 */
public interface AuthenticationService {

	/**
	 * Checks whether the user of the application represented by {@link ClientSession}
	 * object is currently authenticated.
	 * 
	 * @param session {@link ClientSession} object which represents particular
	 *                    user of the application.
	 * @return {@code true} if user is authenticated, {@code false} otherwise.
	 */
	boolean isAuthenticated(ClientSession session);

	/**
	 * Authenticate user which is represented by specified {@link ClientSession}
	 * instance inside application using provided {@code credentials} parameter.
	 * 
	 * @param credentials user provided credentials.
	 * @param session {@link ClientSession} object which represents particular
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
	 * represented by the provided {@link ClientSession} object.
	 * 
	 * @param userSession {@link ClientSession} object which represents particular
	 *                    user of the application.
	 * @return {@link ClientAccount} object if the user represented by provided
	 *         {@link ClientSession} object is authenticated, or {@code null}
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