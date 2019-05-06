package com.revenat.ishop.service.application.impl;

import com.revenat.ishop.service.application.AuthenticationService.Credentials;

/**
 * This component represents user credentials in for of authentication token.
 * 
 * @author Vitaly Dragun
 *
 */
public class AuthenticationTokenCredentials implements Credentials {

	private final String authToken;

	public AuthenticationTokenCredentials(String authToken) {
		this.authToken = authToken;
	}

	public String getAuthToken() {
		return authToken;
	}
}
