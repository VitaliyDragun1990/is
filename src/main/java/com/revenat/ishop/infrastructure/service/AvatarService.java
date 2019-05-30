package com.revenat.ishop.infrastructure.service;

import java.io.IOException;

/**
 * Responsible for downloading avatar images from some url and saving them to
 * some kind of store.
 * 
 * @author Vitaly Dragun
 *
 */
public interface AvatarService {
	int AVATAR_SIZE_IN_PX = 50;
	String MEDIA_AVATAR_PREFIX = "/media/avatar/";

	/**
	 * Downloads user avatar image using provided {@code url}.
	 * 
	 * @param url url of the avatar image to download
	 * @return string representing path to downloaded avatar or {@code null} if
	 *         privided {@code url} was {@code null} in the first place.
	 * @throws IOException
	 */
	String downloadAvatar(String url) throws IOException;

	/**
	 * Deletes avatar image that can be found using provided {@code avatarPath}.
	 * 
	 * @param avatarPath path to find avatar image to delete
	 * @return {@code true} if avatar image has been deleted, {@code false}
	 *         otherwise
	 */
	boolean deleteAvatarIfExists(String avatarPath);
}
