package com.revenat.ishop.infrastructure.service.impl;

import com.revenat.ishop.infrastructure.service.SocialService;

public final class ServiceFactory {

	public static SocialService createSocialSevice(String appId, String secret, String redirectUrl) {
		return new FacebookSocialService(appId, secret, redirectUrl);
	}
	
	private ServiceFactory() {}
}
