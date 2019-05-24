package com.revenat.ishop.infrastructure.service;

import java.io.IOException;

public interface AvatarService {
	int AVATAR_SIZE_IN_PX = 50;
	String MEDIA_AVATAR_PREFIX = "/media/avatar/";
	
	String downloadAvatar(String url) throws IOException;
	
	boolean deleteAvatarIfExists(String avatarPath);
}
