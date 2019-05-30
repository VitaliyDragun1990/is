package com.revenat.ishop.infrastructure.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.di.Value;
import com.revenat.ishop.infrastructure.service.AvatarService;

import net.coobird.thumbnailator.Thumbnails;

@Component
public class FileStorageAvatarService implements AvatarService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageAvatarService.class);
	
	@Value("app.root.dir")
	private String mediaDirParent;
	
	private String getMediaDirParent() {
		return normalizeMediaDirPath(mediaDirParent);
	}

	@Override
	public String saveAvatar(String url) throws IOException {
		if (url != null) {
			String uid = UUID.randomUUID().toString() + ".jpg";
			String fullImgPath = getMediaDirParent() + MEDIA_AVATAR_PREFIX + uid;
			downloadImageFromUrl(url, fullImgPath);
			Thumbnails.of(new File(fullImgPath)).size(AVATAR_SIZE_IN_PX, AVATAR_SIZE_IN_PX).toFile(new File(fullImgPath));
			return MEDIA_AVATAR_PREFIX + uid;
		}
		return null;
	}

	@Override
	public boolean deleteAvatarIfExists(String avatarPath) {
		if (avatarPath != null) {
			File avatar = new File(getMediaDirParent() + avatarPath);
			if (avatar.exists()) {
				if (avatar.delete()) {
					LOGGER.info("Avatar {} has been deleted.", avatarPath);
					return true;
				} else {
					LOGGER.error("Can not delete file: {}", avatar.getAbsolutePath());
				}
			}
		}
		return false;
	}
	
	private void downloadImageFromUrl(String url, String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try (InputStream in = new URL(url).openStream()) {
			Files.copy(in, Paths.get(fileName));
		}
	}

	private String normalizeMediaDirPath(String path) {
		path = path.replace("\\", "/");
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}
}
