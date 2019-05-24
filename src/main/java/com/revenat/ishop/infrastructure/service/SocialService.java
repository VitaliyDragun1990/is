package com.revenat.ishop.infrastructure.service;

import com.revenat.ishop.infrastructure.exception.security.AuthenticationException;

/**
 * This interface represents component responsible for getting access to
 * client's account from some sort of social network.
 * 
 * @author Vitaly Dragun
 *
 */
public interface SocialService {

	/**
	 * Returns authorization url to which clients should be redirected in order to
	 * authenticate themselves as users of some social network.
	 * 
	 */
	String getAuthorizeUrl();

	/**
	 * Gets {@link SocialAccount} of authenticated social network user using
	 * provided {@code authToken} parameter.
	 * 
	 * @param authToken authentication token that will be used to get user's account
	 *                  in social network.
	 * @return {@link SocialAccount} instance that represents user's social network
	 *         account
	 * @throws AuthenticationException if error occurs while getting information
	 *                                 about client's social network account
	 */
	SocialAccount getSocialAccount(String authToken);
}
