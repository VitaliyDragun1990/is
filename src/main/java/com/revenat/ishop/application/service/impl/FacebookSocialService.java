package com.revenat.ishop.application.service.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import com.revenat.ishop.application.infra.exception.security.AuthenticationException;
import com.revenat.ishop.application.service.SocialAccount;
import com.revenat.ishop.application.service.SocialService;

/**
 * Implementation of the {@link SocialService} using Facebook social network to
 * authorize application clients.
 * 
 * @author Vitaly Dragun
 *
 */
class FacebookSocialService implements SocialService {
	private final String appId;
	private final String secret;
	private final String redirectUrl;

	public FacebookSocialService(String appId, String secret, String redirectUrl) {
		this.appId = appId;
		this.secret = secret;
		this.redirectUrl = redirectUrl;
	}

	@Override
	public String getAuthorizeUrl() {
		ScopeBuilder scopeBuilder = new ScopeBuilder();
		scopeBuilder.addPermission(FacebookPermissions.EMAIL);
		FacebookClient client = new DefaultFacebookClient(Version.LATEST);
		return client.getLoginDialogUrl(appId, redirectUrl, scopeBuilder);
	}

	@Override
	public SocialAccount getSocialAccount(String authToken) {
		FacebookClient client = new DefaultFacebookClient(Version.LATEST);
		try {
			AccessToken accessToken = client.obtainUserAccessToken(appId, secret, redirectUrl, authToken);
			client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
			User user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email,first_name,last_name"));
			return new SocialAccount(user.getFirstName(), user.getEmail());
		} catch (FacebookException e) {
			throw new AuthenticationException("Error while retrieving data from user's Facebook account", e);
		}
	}
}
