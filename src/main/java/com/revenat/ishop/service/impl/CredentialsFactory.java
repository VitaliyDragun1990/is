package com.revenat.ishop.service.impl;

import com.revenat.ishop.service.Credentials;

/**
 * This factory is responsible for building different {@link Credentials}
 * implementations.
 * 
 * @author Vitaly Dragun
 *
 */
public final class CredentialsFactory {

	public static Credentials fromAuthToken(String authToken) {
		return new AuthenticationTokenCredentials(authToken);
	}

	private CredentialsFactory() {
	}
}
