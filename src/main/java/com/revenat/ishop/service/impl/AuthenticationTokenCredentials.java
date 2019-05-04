package com.revenat.ishop.service.impl;

import com.revenat.ishop.service.Credentials;

/**
 * This component represents user credentials in for of authentication token.
 * 
 * @author Vitaly Dragun
 *
 */
class AuthenticationTokenCredentials implements Credentials {

	private final String authToken;

	public AuthenticationTokenCredentials(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthToken() {
		return authToken;
	}
}
