package com.revenat.ishop.infrastructure.service.impl;

import java.util.Properties;

import com.revenat.ishop.infrastructure.service.AvatarService;
import com.revenat.ishop.infrastructure.service.NotificationService;
import com.revenat.ishop.infrastructure.service.SocialService;

public final class ServiceFactory {

	public static SocialService createSocialSevice(String appId, String secret, String redirectUrl) {
		return new FacebookSocialService(appId, secret, redirectUrl);
	}
	
	public static AvatarService createAvatarService(String appRootDirectoryPath) {
		return new FileStorageAvatarService(appRootDirectoryPath);
	}
	
	public static NotificationService createNotificationService(Properties props) {
		return new AsyncEmailNotificationService(props);
	}
	
	private ServiceFactory() {}
}
