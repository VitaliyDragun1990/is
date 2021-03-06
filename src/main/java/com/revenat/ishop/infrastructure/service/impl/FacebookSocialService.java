package com.revenat.ishop.infrastructure.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.scope.FacebookPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import com.revenat.ishop.application.exception.security.AuthenticationException;
import com.revenat.ishop.infrastructure.service.SocialAccount;
import com.revenat.ishop.infrastructure.service.SocialService;

/**
 * Implementation of the {@link SocialService} using Facebook social network to
 * authorize application clients.
 * 
 * @author Vitaly Dragun
 *
 */
@Service
public class FacebookSocialService implements SocialService {
//	@Value("${social.facebook.appId}")
	private String appId;
//	@Value("${social.facebook.secret}")
	private String secret;
//	@Value("${app.host}")
	private String host;
//	@Value("${social.redirectUri}")
	private String redirectUri;
	
	@Autowired
	public FacebookSocialService(
			@Value("${social.facebook.appId}") String appId,
			@Value("${social.facebook.secret}") String secret,
			@Value("${app.host}") String host,
			@Value("${social.redirectUri}") String redirectUri) {
		this.appId = appId;
		this.secret = secret;
		this.host = host;
		this.redirectUri = redirectUri;
	}

	private String getRedirectUrl() {
		return host + redirectUri;
	}

	@Override
	public String getAuthorizeUrl() {
		ScopeBuilder scopeBuilder = new ScopeBuilder();
		scopeBuilder.addPermission(FacebookPermissions.EMAIL);
		FacebookClient client = new DefaultFacebookClient(Version.LATEST);
		return client.getLoginDialogUrl(appId, getRedirectUrl(), scopeBuilder);
	}

	@Override
	public SocialAccount getSocialAccount(String authToken) {
		FacebookClient client = new DefaultFacebookClient(Version.LATEST);
		try {
			AccessToken accessToken = client.obtainUserAccessToken(appId, secret, getRedirectUrl(), authToken);
			client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
			User user = client.fetchObject("me", User.class, Parameter.with("fields", "name,email,first_name,last_name"));
			String avatarUrl = String.format("https://graph.facebook.com/v3.3/%s/picture?type=small", user.getId());
			return new SocialAccount(user.getFirstName(), user.getEmail(), avatarUrl);
		} catch (FacebookException e) {
			throw new AuthenticationException("Error while retrieving data from user's Facebook account", e);
		}
	}
}
